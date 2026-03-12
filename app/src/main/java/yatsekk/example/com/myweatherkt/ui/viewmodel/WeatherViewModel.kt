package yatsekk.example.com.myweatherkt.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yatsekk.example.com.myweatherkt.BuildConfig
import yatsekk.example.com.myweatherkt.data.api.model.ForecastDto
import yatsekk.example.com.myweatherkt.data.api.model.WeatherDto
import yatsekk.example.com.myweatherkt.data.repository.WeatherRepository
import yatsekk.example.com.myweatherkt.util.CityPreferences

sealed class WeatherUiState {
    data object Idle : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val weather: WeatherDto) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

sealed class ForecastUiState {
    data object Idle : ForecastUiState()
    data object Loading : ForecastUiState()
    data class Success(val forecast: ForecastDto) : ForecastUiState()
    data class Error(val message: String) : ForecastUiState()
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    private val cityPreferences = CityPreferences(application)

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _forecastUiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Idle)
    val forecastUiState: StateFlow<ForecastUiState> = _forecastUiState

    private val _cityInput = MutableStateFlow(cityPreferences.getSavedCity())
    val cityInput: StateFlow<String> = _cityInput

    fun onCityInputChanged(value: String) {
        _cityInput.value = value
    }

    fun loadWeather() {
        val city = _cityInput.value.trim()
        if (city.isEmpty()) return

        cityPreferences.saveCity(city)
        _uiState.value = WeatherUiState.Loading
        _forecastUiState.value = ForecastUiState.Loading
        viewModelScope.launch {
            val weatherDeferred = async {
                runCatching { repository.getWeather(city, BuildConfig.WEATHER_API_KEY) }
            }
            val forecastDeferred = async {
                runCatching { repository.getForecast(city, BuildConfig.WEATHER_API_KEY) }
            }

            weatherDeferred.await().fold(
                onSuccess = { _uiState.value = WeatherUiState.Success(it) },
                onFailure = { _uiState.value = WeatherUiState.Error(it.message ?: "Unknown error") }
            )
            forecastDeferred.await().fold(
                onSuccess = { _forecastUiState.value = ForecastUiState.Success(it) },
                onFailure = { _forecastUiState.value = ForecastUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}
