package models

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherAPIResponse (

    @SerialName("latitude"              ) var latitude             : Double?       = null,
    @SerialName("longitude"             ) var longitude            : Double?       = null,
    @SerialName("generationtime_ms"     ) var generationtimeMs     : Double?       = null,
    @SerialName("utc_offset_seconds"    ) var utcOffsetSeconds     : Int?          = null,
    @SerialName("timezone"              ) var timezone             : String?       = null,
    @SerialName("timezone_abbreviation" ) var timezoneAbbreviation : String?       = null,
    @SerialName("elevation"             ) var elevation            : Double?          = null,
    @SerialName("current_units"         ) var currentUnits         : CurrentUnits? = CurrentUnits(),
    @SerialName("current"               ) var current              : Current?      = Current(),
    @SerialName("hourly_units"          ) var hourlyUnits          : HourlyUnits?  = HourlyUnits(),
    @SerialName("hourly"                ) var hourly               : Hourly?       = Hourly(),
    @SerialName("daily_units"           ) var dailyUnits           : DailyUnits?   = DailyUnits(),
    @SerialName("daily"                 ) var daily                : Daily?        = Daily()

)

@Serializable
data class CurrentUnits (

    @SerialName("time"           ) var time          : String? = null,
    @SerialName("interval"       ) var interval      : String? = null,
    @SerialName("temperature_2m" ) var temperature2m : String? = null,
    @SerialName("weather_code" ) var weatherCode : String? = null

)

@Serializable
data class Current (

    @SerialName("time"           ) var time          : String? = null,
    @SerialName("interval"       ) var interval      : Int?    = null,
    @SerialName("temperature_2m" ) var temperature2m : Double? = null,
    @SerialName("weather_code" ) var weatherCode : Int? = null,
    @SerialName("relative_humidity_2m") var humidity : Int? = null,
    @SerialName("apparent_temperature") var feelsLike : Double? = null

)

@Serializable
data class HourlyUnits (

    @SerialName("time"           ) var time          : String? = null,
    @SerialName("temperature_2m" ) var temperature2m : String? = null,
    @SerialName("weather_code" ) var weatherCode : String? = null

)

@Serializable
data class Hourly (

    @SerialName("time"           ) var time          : ArrayList<String> = arrayListOf(),
    @SerialName("temperature_2m" ) var temperature2m : ArrayList<Double> = arrayListOf(),
    @SerialName("weather_code" ) var weatherCode : ArrayList<Int> = arrayListOf(),
    @SerialName("visibility" ) var visibility : ArrayList<Double> = arrayListOf(),
    @SerialName("wind_speed_10m" ) var windSpeed : ArrayList<Double> = arrayListOf()

)

@Serializable
data class DailyUnits (

    @SerialName("time"               ) var time             : String? = null,
    @SerialName("temperature_2m_max" ) var temperature2mMax : String? = null,
    @SerialName("temperature_2m_min" ) var temperature2mMin : String? = null,
    @SerialName("sunrise" ) var sunrise : String? = null,
    @SerialName("sunset" ) var sunset : String? = null,
    @SerialName("uv_index_max" ) var uvIndex : String? = null

)

@Serializable
data class Daily (

    @SerialName("time"               ) var time             : ArrayList<String> = arrayListOf(),
    @SerialName("temperature_2m_max" ) var temperature2mMax : ArrayList<Double> = arrayListOf(),
    @SerialName("temperature_2m_min" ) var temperature2mMin : ArrayList<Double> = arrayListOf(),
    @SerialName("weather_code" ) var weatherCode : ArrayList<Int> = arrayListOf(),
    @SerialName("sunrise" ) var sunrise : ArrayList<String> = arrayListOf(),
    @SerialName("sunset" ) var sunset : ArrayList<String> = arrayListOf(),
    @SerialName("uv_index_max" ) var uvIndex : ArrayList<Double> = arrayListOf()

)

fun weatherCodeToDescription(weatherCode: Int): String {
    return when (weatherCode) {
        0 -> "Clear sky"
        1 -> "Mainly clear"
        2 -> "partly cloudy"
        3 -> "overcast"
        45 -> "Fog"
        48 -> "depositing rime fog"
        51 -> "Drizzle light"
        53 -> " Drizzle moderate"
        55 -> "Drizzle dense intensity"
        56 -> "Freezing drizzle light"
        57 -> "Freezing drizzle dense intensity"
        61 -> "Rain slight"
        63 -> "Rain moderate"
        65 -> "Rain heavy"
        66 -> "Freezing rain light"
        67 -> "Freezing rain heavy"
        71 -> "Snow fall slight"
        73 -> "Snow fall moderate"
        75 -> "Snow fall heavy"
        77 -> "Snow grains"
        80 -> "Rain showers slight"
        81 -> "Rain showers moderate"
        82 -> "Rain showers violent"
        85 -> "Snow showers slight"
        86 -> "Snow showers heavy"
        95 -> "Thunderstorm"
        96 -> "Thunderstorm with slight"
        99 -> "Thunderstorm with heavy hail"
        else -> "clear sky"
    }
}
