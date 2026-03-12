package yatsekk.example.com.myweatherkt.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class HourlyWeatherItem(
    val hour: String,
    val temp: Int,
    val condition: String,
    val wind: String
)

@Composable
fun HourlyWeatherScreen(modifier: Modifier = Modifier) {
    val items = remember { buildStaticHourlyData() }

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

private fun buildStaticHourlyData(): List<HourlyWeatherItem> = listOf(
    HourlyWeatherItem("22:00", 9, "Cloudy", "Wind: 5 m/s"),
    HourlyWeatherItem("23:00", 8, "Cloudy", "Wind: 5 m/s"),
    HourlyWeatherItem("00:00", 8, "Cloudy", "Wind: 4 m/s"),
    HourlyWeatherItem("01:00", 7, "Cloudy", "Wind: 4 m/s"),
    HourlyWeatherItem("02:00", 6, "Cloudy", "Wind: 3 m/s"),
    HourlyWeatherItem("03:00", 6, "Cloudy", "Wind: 3 m/s"),
    HourlyWeatherItem("04:00", 6, "Cloudy", "Wind: 2 m/s"),
    HourlyWeatherItem("05:00", 5, "Cloudy", "Wind: 0 m/s"),
    HourlyWeatherItem("06:00", 5, "Cloudy", "Wind: 0 m/s"),
    HourlyWeatherItem("07:00", 5, "Cloudy", "Wind: 1 m/s"),
    HourlyWeatherItem("08:00", 5, "Cloudy", "Wind: 1 m/s"),
    HourlyWeatherItem("09:00", 6, "Cloudy", "Wind: 2 m/s"),
    HourlyWeatherItem("10:00", 5, "Cloudy with light rain", "Wind: 2 m/s"),
    HourlyWeatherItem("11:00", 5, "Cloudy with light rain", "Wind: 3 m/s"),
    HourlyWeatherItem("12:00", 5, "Cloudy with light rain", "Wind: 3 m/s"),
    HourlyWeatherItem("13:00", 5, "Cloudy with light rain", "Wind: 4 m/s"),
    HourlyWeatherItem("14:00", 5, "Cloudy with light rain", "Wind: 4 m/s"),
    HourlyWeatherItem("15:00", 5, "Cloudy with light rain", "Wind: 5 m/s"),
    HourlyWeatherItem("16:00", 5, "Partly cloudy with light drizzle", "Wind: 6 m/s")
)
