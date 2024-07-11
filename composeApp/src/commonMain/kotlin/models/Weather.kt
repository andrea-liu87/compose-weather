package models

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherAPIResponse (

    @SerialName("lat"             ) var lat            : Double?             = null,
    @SerialName("lon"             ) var lon            : Double?             = null,
    @SerialName("timezone"        ) var timezone       : String?             = null,
    @SerialName("timezone_offset" ) var timezoneOffset : Int?                = null,
    @SerialName("current"         ) var current        : Current?            = Current(),
    @SerialName("minutely"        ) var minutely       : ArrayList<Minutely> = arrayListOf(),
    @SerialName("hourly"          ) var hourly         : ArrayList<Hourly>   = arrayListOf(),
    @SerialName("daily"           ) var daily          : ArrayList<Daily>    = arrayListOf()

)

@Serializable
data class Weather (
    @SerialName("id"          ) var id          : Int?    = null,
    @SerialName("main"        ) var main        : String? = null,
    @SerialName("description" ) var description : String? = null,
    @SerialName("icon"        ) var icon        : String? = null
)

@Serializable
data class Current (

    @SerialName("dt"         ) var dt         : Int?               = null,
    @SerialName("sunrise"    ) var sunrise    : Int?               = null,
    @SerialName("sunset"     ) var sunset     : Int?               = null,
    @SerialName("temp"       ) var temp       : Double?            = null,
    @SerialName("feels_like" ) var feelsLike  : Double?            = null,
    @SerialName("pressure"   ) var pressure   : Int?               = null,
    @SerialName("humidity"   ) var humidity   : Int?               = null,
    @SerialName("dew_point"  ) var dewPoint   : Double?            = null,
    @SerialName("uvi"        ) var uvi        : Double?               = null,
    @SerialName("clouds"     ) var clouds     : Int?               = null,
    @SerialName("visibility" ) var visibility : Int?               = null,
    @SerialName("wind_speed" ) var windSpeed  : Double?            = null,
    @SerialName("wind_deg"   ) var windDeg    : Int?               = null,
    @SerialName("wind_gust"  ) var windGust   : Double?            = null,
    @SerialName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf()

)

@Serializable
data class Minutely (

    @SerialName("dt"            ) var dt            : Int? = null,
    @SerialName("precipitation" ) var precipitation : Int? = null

)

@Serializable
data class Hourly (

    @SerialName("dt"         ) var dt         : Int?               = null,
    @SerialName("temp"       ) var temp       : Double?            = null,
    @SerialName("feels_like" ) var feelsLike  : Double?            = null,
    @SerialName("pressure"   ) var pressure   : Int?               = null,
    @SerialName("humidity"   ) var humidity   : Int?               = null,
    @SerialName("dew_point"  ) var dewPoint   : Double?            = null,
    @SerialName("uvi"        ) var uvi        : Double?               = null,
    @SerialName("clouds"     ) var clouds     : Int?               = null,
    @SerialName("visibility" ) var visibility : Int?               = null,
    @SerialName("wind_speed" ) var windSpeed  : Double?            = null,
    @SerialName("wind_deg"   ) var windDeg    : Int?               = null,
    @SerialName("wind_gust"  ) var windGust   : Double?            = null,
    @SerialName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
    @SerialName("pop"        ) var pop        : Double?               = null,
    @SerialName("snow"       ) var snow       : Snow?              = Snow()

)

@Serializable
data class Daily (

    @SerialName("dt"         ) var dt        : Int?               = null,
    @SerialName("sunrise"    ) var sunrise   : Int?               = null,
    @SerialName("sunset"     ) var sunset    : Int?               = null,
    @SerialName("moonrise"   ) var moonrise  : Int?               = null,
    @SerialName("moonset"    ) var moonset   : Int?               = null,
    @SerialName("moon_phase" ) var moonPhase : Double?            = null,
    @SerialName("summary"    ) var summary   : String?            = null,
    @SerialName("temp"       ) var temp      : Temp?              = Temp(),
    @SerialName("feels_like" ) var feelsLike : FeelsLike?         = FeelsLike(),
    @SerialName("pressure"   ) var pressure  : Int?               = null,
    @SerialName("humidity"   ) var humidity  : Int?               = null,
    @SerialName("dew_point"  ) var dewPoint  : Double?            = null,
    @SerialName("wind_speed" ) var windSpeed : Double?            = null,
    @SerialName("wind_deg"   ) var windDeg   : Int?               = null,
    @SerialName("wind_gust"  ) var windGust  : Double?            = null,
    @SerialName("weather"    ) var weather   : ArrayList<Weather> = arrayListOf(),
    @SerialName("clouds"     ) var clouds    : Int?               = null,
    @SerialName("pop"        ) var pop       : Double?            = null,
    @SerialName("uvi"        ) var uvi       : Double?            = null

)



@Serializable
data class Main (
    @SerialName("temp"       ) var temp      : Double? = null,
    @SerialName("feels_like" ) var feelsLike : Double? = null,
    @SerialName("temp_min"   ) var tempMin   : Double? = null,
    @SerialName("temp_max"   ) var tempMax   : Double? = null,
    @SerialName("pressure"   ) var pressure  : Int?    = null,
    @SerialName("humidity"   ) var humidity  : Int?    = null,
    @SerialName("sea_level"  ) var seaLevel  : Int?    = null,
    @SerialName("grnd_level" ) var grndLevel : Int?    = null
)

@Serializable
data class FeelsLike (

    @SerialName("day"   ) var day   : Double? = null,
    @SerialName("night" ) var night : Double? = null,
    @SerialName("eve"   ) var eve   : Double? = null,
    @SerialName("morn"  ) var morn  : Double? = null

)

@Serializable
data class Temp (

    @SerialName("day"   ) var day   : Double? = null,
    @SerialName("min"   ) var min   : Double? = null,
    @SerialName("max"   ) var max   : Double? = null,
    @SerialName("night" ) var night : Double? = null,
    @SerialName("eve"   ) var eve   : Double? = null,
    @SerialName("morn"  ) var morn  : Double? = null

)

@Serializable
data class Wind (
    @SerialName("speed" ) var speed : Double? = null,
    @SerialName("deg"   ) var deg   : Int?    = null,
    @SerialName("gust"  ) var gust  : Double? = null
)

@Serializable
data class Rain (
    @SerialName("1h" ) var h : Double? = null
)

@Serializable
data class Snow (
    @SerialName("1h" ) var h : Double? = null
)

@Serializable
data class Clouds (
    @SerialName("all" ) var all : Int? = null
)

@Serializable
data class Sys (
    @SerialName("type"    ) var type    : Int?    = null,
    @SerialName("id"      ) var id      : Int?    = null,
    @SerialName("country" ) var country : String? = null,
    @SerialName("sunrise" ) var sunrise : Int?    = null,
    @SerialName("sunset"  ) var sunset  : Int?    = null
)