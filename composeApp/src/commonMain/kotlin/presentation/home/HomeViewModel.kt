package presentation.home

import data.LocationService
import data.api.WeatherAPI
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import data.weatherDataSaved
import io.github.xxfast.decompose.router.SavedStateHandle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import models.Current
import models.Daily
import models.Location
import models.Temp
import models.Weather
import models.WeatherAPIResponse
import presentation.navigation.ViewModel

class HomeViewModel(savedState: SavedStateHandle) : ViewModel() {
    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
    private val initialState: HomeState = savedState.get() ?: HomeState(
        WeatherAPIResponse(
            timezone =  "America",
            current = Current(temp = 230.0, weather = arrayListOf( Weather(description = "Cloudy Day"))),
            daily = arrayListOf(Daily(temp = Temp(max = 270.0, min = 200.0)))
        ),
        Location(
            latitude = 45.5019,
            longitude = -73.5674,
            name = "Montreal"
        )
    )
    private val webService = WeatherAPI()
    private val locationService = LocationService()

    val states: StateFlow<HomeState> by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            HomeDomain(initialState, eventsFlow, webService, locationService, weatherDataSaved) }
            .onEach { state -> savedState.set(state) }
            .stateIn(this, SharingStarted.Lazily, initialState)
    }

    fun onRefresh() { launch { eventsFlow.emit(HomeEvent.Refresh) } }
}