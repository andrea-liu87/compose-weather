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
import models.Hourly
import models.Location
import models.WeatherAPIResponse
import presentation.navigation.ViewModel

class HomeViewModel(savedState: RouterContext) : ViewModel() {
    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
     val initialHomeState = HomeState(
        WeatherAPIResponse(
            timezone =  "America",
            current = Current(time = "2024-07-14T20:45", interval = 900, temperature2m = 33.7, weatherCode = 77),
            daily = Daily(time = arrayListOf("2024-07-15"), temperature2mMax = arrayListOf(40.0), arrayListOf(32.3), weatherCode = arrayListOf(1)),
            hourly = Hourly(time = arrayListOf("2024-07-15T09:00"), temperature2m = arrayListOf(40.0), weatherCode = arrayListOf(1))
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