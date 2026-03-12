package yatsekk.example.com.myweatherkt.data.api.model

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("list") val list: List<ForecastItemDto>,
    @SerializedName("city") val city: ForecastCityDto
)

data class ForecastItemDto(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherConditionDto>,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("dt_txt") val dtTxt: String
)

data class ForecastCityDto(
    @SerializedName("name") val name: String
)
