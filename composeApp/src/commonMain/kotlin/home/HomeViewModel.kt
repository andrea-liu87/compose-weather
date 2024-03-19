package home

import LocationService
import api.WeatherAPI
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import io.github.xxfast.decompose.router.SavedStateHandle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import models.Current
import models.Daily
import models.Main
import models.Temp
import models.Weather
import models.WeatherAPIResponse
import navigation.ViewModel

class HomeViewModel(savedState: SavedStateHandle) : ViewModel() {
    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
    private val initialState: HomeState = savedState.get() ?: HomeState(
        WeatherAPIResponse(
            timezone =  "Montreal",
            current = Current(temp = 230.0, weather = arrayListOf( Weather(description = "Cloudy Day"))),
            daily = arrayListOf(Daily(temp = Temp(max = 270.0, min = 200.0)))
        )
    )
    private val webService = WeatherAPI()
    private val locationService = LocationService()

    val states: StateFlow<HomeState> by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            HomeDomain(initialState, eventsFlow, webService, locationService) }
            .onEach { state -> savedState.set(state) }
            .stateIn(this, SharingStarted.Lazily, initialState)
    }

    fun onRefresh() { launch { eventsFlow.emit(HomeEvent.Refresh) } }
}