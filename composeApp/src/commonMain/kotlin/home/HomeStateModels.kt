package home

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import models.Location
import models.WeatherAPIResponse

val Loading: Nothing? = null

@Parcelize
data class HomeState(
    val weatherData: WeatherAPIResponse?,
    val location: Location?
): Parcelable

sealed interface HomeEvent {
    object Refresh: HomeEvent
}