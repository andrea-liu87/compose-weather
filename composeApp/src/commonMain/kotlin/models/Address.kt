package models

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GeocodingResponse (

    @SerialName("plus_code" ) var plusCode : PlusCode?          = PlusCode(),
    @SerialName("results"   ) var results  : ArrayList<Results> = arrayListOf(),
    @SerialName("status"    ) var status   : String?            = null

): Parcelable
@Serializable
@Parcelize
data class Results (

    @SerialName("address_components" ) var addressComponents : ArrayList<AddressComponents> = arrayListOf(),
    @SerialName("formatted_address"  ) var formattedAddress  : String?                      = null,
    @SerialName("geometry"           ) var geometry          : Geometry?                    = Geometry(),
    @SerialName("place_id"           ) var placeId           : String?                      = null,
    @SerialName("plus_code"          ) var plusCode          : PlusCode?                    = PlusCode(),
    @SerialName("types"              ) var types             : ArrayList<String>            = arrayListOf()

): Parcelable

@Serializable
@Parcelize
data class AddressComponents (

    @SerialName("long_name"  ) var longName  : String?           = null,
    @SerialName("short_name" ) var shortName : String?           = null,
    @SerialName("types"      ) var types     : ArrayList<String> = arrayListOf()

): Parcelable

@Serializable
@Parcelize
data class Geometry (

    @SerialName("location"      ) var location     : Location? = Location(0.00, 0.00),
    @SerialName("location_type" ) var locationType : String?   = null,
    @SerialName("viewport"      ) var viewport     : Viewport? = Viewport()

): Parcelable


@Serializable
@Parcelize
data class PlusCode (

    @SerialName("compound_code" ) var compoundCode : String? = null,
    @SerialName("global_code"   ) var globalCode   : String? = null

): Parcelable

@Serializable
@Parcelize
data class Northeast (

    @SerialName("lat" ) var lat : Double? = null,
    @SerialName("lng" ) var lng : Double? = null

): Parcelable

@Serializable
@Parcelize
data class Southwest (

    @SerialName("lat" ) var lat : Double? = null,
    @SerialName("lng" ) var lng : Double? = null

): Parcelable

@Serializable
@Parcelize
data class Viewport (

    @SerialName("northeast" ) var northeast : Northeast? = Northeast(),
    @SerialName("southwest" ) var southwest : Southwest? = Southwest()

): Parcelable