package models

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null
): Parcelable