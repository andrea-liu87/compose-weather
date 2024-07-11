package presentation.home

import data.LocationService
import data.api.WeatherAPI
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import data.weatherDataSaved
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.getOrCreate
import io.github.xxfast.decompose.router.state
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import models.Current
import models.Daily
import models.Location
import models.Temp
import models.Weather
import models.WeatherAPIResponse
import presentation.navigation.ViewModel

class HomeViewModel(savedState: RouterContext) : ViewModel() {
    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
     val initialHomeState = HomeState(
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
    private val initialState: HomeState = savedState.state(initialHomeState){states.value}
    private val webService = WeatherAPI()
    private val locationService = LocationService()

    val states: StateFlow<HomeState> by lazy {
        moleculeFlow(RecompositionMode.Immediate) {
            HomeDomain(initialState, eventsFlow, webService, locationService, weatherDataSaved) }
            .onEach { state -> savedState.getOrCreate(key = "home", {state}) }
            .stateIn(this, SharingStarted.Lazily, initialState)
    }

    fun onRefresh() { launch { eventsFlow.emit(HomeEvent.Refresh) } }
}