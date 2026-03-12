package yatsekk.example.com.myweatherkt.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yatsekk.example.com.myweatherkt.BuildConfig
import yatsekk.example.com.myweatherkt.data.api.model.WeatherDto
import yatsekk.example.com.myweatherkt.data.repository.WeatherRepository
import yatsekk.example.com.myweatherkt.util.CityPreferences

sealed class WeatherUiState {
    data object Idle : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val weather: WeatherDto) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    private val cityPreferences = CityPreferences(application)

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState

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
        viewModelScope.launch {
            try {
                val weather = repository.getWeather(city, BuildConfig.WEATHER_API_KEY)
                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
