package yatsekk.example.com.myweatherkt.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import yatsekk.example.com.myweatherkt.R
import yatsekk.example.com.myweatherkt.data.api.model.ForecastDto
import yatsekk.example.com.myweatherkt.ui.viewmodel.ForecastUiState
import yatsekk.example.com.myweatherkt.ui.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class HourlyWeatherItem(
    val hour: String,
    val temp: Int,
    val condition: String,
    val wind: String
)

@Composable
fun HourlyWeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel()
) {
    val forecastState by viewModel.forecastUiState.collectAsState()

    when (val state = forecastState) {
        is ForecastUiState.Idle -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.enter_city_prompt),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        is ForecastUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ForecastUiState.Success -> {
            HourlyWeatherList(modifier = modifier, forecast = state.forecast)
        }
        is ForecastUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.error_loading_data),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun HourlyWeatherList(modifier: Modifier = Modifier, forecast: ForecastDto) {
    val items = buildHourlyItems(forecast)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            HourlyWeatherCard(item)
        }
    }
}

@Composable
private fun HourlyWeatherCard(item: HourlyWeatherItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.hour,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.15f)
            )
            Text(
                text = "${item.temp}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(0.15f)
            )
            Text(
                text = item.condition,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.45f)
            )
            Text(
                text = item.wind,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.25f)
            )
        }
    }
}

private fun buildHourlyItems(forecast: ForecastDto): List<HourlyWeatherItem> {
    val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return forecast.list.map { item ->
        val hour = hourFormat.format(Date(item.dt * 1000L))
        val temp = item.main.temp.roundToInt()
        val condition = item.weather.firstOrNull()?.description
            ?.replaceFirstChar { it.uppercase() } ?: ""
        val wind = "Wind: ${item.wind.speed} m/s"
        HourlyWeatherItem(hour = hour, temp = temp, condition = condition, wind = wind)
    }
}
