package home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import api.WeatherAPI
import kotlinx.coroutines.flow.Flow
import models.WeatherAPIResponse

@Composable
fun HomeDomain(
    initialState: HomeState,
    events: Flow<HomeEvent>,
    webService: WeatherAPI
): HomeState {
    val weather: WeatherAPIResponse? by remember { mutableStateOf(initialState.weatherData) }

    return HomeState(weather)
}