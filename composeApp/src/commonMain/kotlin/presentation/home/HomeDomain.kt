package presentation.home

import data.LocationService
import androidx.compose.runtime.*
import data.api.WeatherAPI
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Location
import models.WeatherAPIResponse
import org.lighthousegames.logging.Logger
import org.lighthousegames.logging.logging

@Composable
fun HomeDomain(
    initialState: HomeState,
    events: Flow<HomeEvent>,
    webService: WeatherAPI,
    locationService: LocationService,
    store: KStore<WeatherAPIResponse>
): HomeState {
    var weather: WeatherAPIResponse? by remember { mutableStateOf(initialState.weatherData) }
    var location: Location? by remember { mutableStateOf(initialState.location) }

    LaunchedEffect(Unit) {
        weather = store.get() ?: Loading

        location = locationService.getCurrentLocationOneTime()
        withContext(Dispatchers.IO){
            weather = webService.getWeatherApiDataFrLonLat(location!!.latitude, location!!.longitude).getOrNull()
            store.update { weather?.copy(timezone = location?.name, latitude = location!!.latitude, longitude = location!!.longitude) }
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when(event) {
                HomeEvent.Refresh -> launch {
                    location = locationService.getCurrentLocationOneTime()
                    withContext(Dispatchers.IO){
                        weather = webService.getWeatherApiDataFrLonLat(location!!.latitude, location!!.longitude).getOrNull()
                        store.update { weather?.copy(timezone = location?.name, latitude = location!!.latitude, longitude = location!!.longitude) }
                    }
                }
            }
        }
    }
    return HomeState(weather, location)
}