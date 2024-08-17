package presentation.home

import models.Location
import models.WeatherAPIResponse

sealed interface HomeState {
    object Loading : HomeState
    data class Error(val message: String) : HomeState
    data class Success(
        val weatherData: WeatherAPIResponse? = null,
        val location: Location? = null) : HomeState
}

sealed interface HomeViewModelActions {
    object RefreshCurrentLocation : HomeViewModelActions
    data class AddNewPlace(val place: String) : HomeViewModelActions
}