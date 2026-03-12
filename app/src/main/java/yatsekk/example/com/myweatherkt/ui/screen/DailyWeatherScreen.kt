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

data class DailyWeatherItem(
    val day: String,
    val tempMin: Int,
    val tempMax: Int,
    val condition: String,
    val wind: String
)

@Composable
fun DailyWeatherScreen(
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
            DailyWeatherList(modifier = modifier, forecast = state.forecast)
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
private fun DailyWeatherList(modifier: Modifier = Modifier, forecast: ForecastDto) {
    val items = buildDailyItems(forecast)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            DailyWeatherCard(item)
        }
    }
}

@Composable
private fun DailyWeatherCard(item: DailyWeatherItem) {
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
                text = item.day,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.20f)
            )
            Text(
                text = "${item.tempMin}°/${item.tempMax}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(0.22f)
            )
            Text(
                text = item.condition,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.35f)
            )
            Text(
                text = item.wind,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.23f)
            )
        }
    }
}

private fun buildDailyItems(forecast: ForecastDto): List<DailyWeatherItem> {
    val dayFormat = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
    val dateKeyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    return forecast.list
        .groupBy { dateKeyFormat.format(Date(it.dt * 1000L)) }
        .map { (_, entries) ->
            val day = dayFormat.format(Date(entries.first().dt * 1000L))
            val tempMin = entries.minOf { it.main.tempMin }.roundToInt()
            val tempMax = entries.maxOf { it.main.tempMax }.roundToInt()
            val condition = entries
                .mapNotNull { it.weather.firstOrNull()?.description }
                .groupingBy { it }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key
                ?.replaceFirstChar { it.uppercase() } ?: ""
            val windAvg = entries.map { it.wind.speed }.average()
            val wind = "Wind: ${"%.1f".format(windAvg)} m/s"
            DailyWeatherItem(
                day = day,
                tempMin = tempMin,
                tempMax = tempMax,
                condition = condition,
                wind = wind
            )
        }
}
