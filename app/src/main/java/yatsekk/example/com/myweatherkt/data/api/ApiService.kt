package yatsekk.example.com.myweatherkt.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import yatsekk.example.com.myweatherkt.data.api.model.WeatherDto

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherDto
}
