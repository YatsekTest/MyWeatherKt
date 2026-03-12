package yatsekk.example.com.myweatherkt.data.repository

import yatsekk.example.com.myweatherkt.data.api.RetrofitClient
import yatsekk.example.com.myweatherkt.data.api.model.ForecastDto
import yatsekk.example.com.myweatherkt.data.api.model.WeatherDto

class WeatherRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getWeather(city: String, apiKey: String): WeatherDto {
        return apiService.getCurrentWeather(city = city, apiKey = apiKey)
    }

    suspend fun getForecast(city: String, apiKey: String): ForecastDto {
        return apiService.getForecast(city = city, apiKey = apiKey)
    }

    suspend fun getWeatherByCoords(lat: Double, lon: Double, apiKey: String): WeatherDto {
        return apiService.getCurrentWeatherByCoords(lat = lat, lon = lon, apiKey = apiKey)
    }

    suspend fun getForecastByCoords(lat: Double, lon: Double, apiKey: String): ForecastDto {
        return apiService.getForecastByCoords(lat = lat, lon = lon, apiKey = apiKey)
    }
}
