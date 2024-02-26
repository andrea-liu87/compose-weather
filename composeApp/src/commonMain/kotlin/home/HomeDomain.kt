package home

import LocationService
import androidx.compose.runtime.*
import api.WeatherAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.WeatherAPIResponse

@Composable
fun HomeDomain(
    initialState: HomeState,
    events: Flow<HomeEvent>,
    webService: WeatherAPI,
    locationService: LocationService
): HomeState {
    var weather: WeatherAPIResponse? by remember { mutableStateOf(initialState.weatherData) }

    LaunchedEffect(Unit) {
        weather = Loading

        val fetchData: WeatherAPIResponse = webService.getWeatherApiData().getOrNull()
            ?: return@LaunchedEffect // TODO: Handle errors

        weather = fetchData
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when(event) {
                HomeEvent.Refresh -> launch {
                    val location = locationService.getCurrentLocationOneTime()
                    withContext(Dispatchers.IO){
                        weather = webService.getWeatherApiDataFrLonLat(location.latitude, location.longitude).getOrNull()
                    }
                }
            }
        }
    }
    return HomeState(weather)
}