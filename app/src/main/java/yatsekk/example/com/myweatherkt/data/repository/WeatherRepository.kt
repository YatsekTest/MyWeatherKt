package yatsekk.example.com.myweatherkt.data.repository

import yatsekk.example.com.myweatherkt.data.api.RetrofitClient
import yatsekk.example.com.myweatherkt.data.api.model.WeatherDto

class WeatherRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getWeather(city: String, apiKey: String): WeatherDto {
        return apiService.getCurrentWeather(city = city, apiKey = apiKey)
    }
}
