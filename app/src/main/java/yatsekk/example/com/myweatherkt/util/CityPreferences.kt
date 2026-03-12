package yatsekk.example.com.myweatherkt.util

import android.content.Context

class CityPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    fun saveCity(city: String) {
        prefs.edit().putString(CITY_KEY, city).apply()
    }

    fun getSavedCity(): String {
        return prefs.getString(CITY_KEY, DEFAULT_CITY) ?: DEFAULT_CITY
    }

    companion object {
        private const val CITY_KEY = "city"
        private const val DEFAULT_CITY = "Vladivostok"
    }
}
