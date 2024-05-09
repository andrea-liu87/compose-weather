package presentation.home

import data.LocationService
import androidx.compose.runtime.*
import data.api.WeatherAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Location
import models.WeatherAPIResponse

@Composable
fun HomeDomain(
    initialState: HomeState,
    events: Flow<HomeEvent>,
    webService: WeatherAPI,
    locationService: LocationService
): HomeState {
    var weather: WeatherAPIResponse? by remember { mutableStateOf(initialState.weatherData) }
    var location: Location? by remember { mutableStateOf(initialState.location) }

    LaunchedEffect(Unit) {
        weather = Loading

        location = locationService.getCurrentLocationOneTime()
        withContext(Dispatchers.IO){
            weather = webService.getWeatherApiDataFrLonLat(location!!.latitude, location!!.longitude).getOrNull()
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when(event) {
                HomeEvent.Refresh -> launch {
                    location = locationService.getCurrentLocationOneTime()
                    withContext(Dispatchers.IO){
                        weather = webService.getWeatherApiDataFrLonLat(location!!.latitude, location!!.longitude).getOrNull()
                    }
                }
            }
        }
    }
    return HomeState(weather, location)
}