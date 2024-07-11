package presentation.home

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import models.Location
import models.WeatherAPIResponse

val Loading: Nothing? = null

@Serializable
data class HomeState(
    val weatherData: WeatherAPIResponse?,
    val location: Location?
)

sealed interface HomeEvent {
    object Refresh: HomeEvent
}