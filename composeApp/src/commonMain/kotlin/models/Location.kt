package models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    @SerialName("lat") val latitude: Double,
    @SerialName("lng") val longitude: Double,
    val name: String? = null
)

@Serializable
data class LocationRequest (

    @SerialName("macAddress"         ) var macAddress         : String? = null,
    @SerialName("signalStrength"     ) var signalStrength     : Int?    = null,
    @SerialName("signalToNoiseRatio" ) var signalToNoiseRatio : Int?    = null,
    @SerialName("channel"            ) var channel            : Int?    = null,
    @SerialName("age"                ) var age                : Int?    = null

)

@Serializable
data class LocationResponse (

    @SerialName("location" ) var location : Location? = null,
    //@SerialName("accuracy" ) var accuracy : Int?      = null

)