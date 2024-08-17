package presentation.home

import data.LocationService
import data.api.WeatherAPI
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import data.weatherDataSaved
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.getOrCreate
import io.github.xxfast.decompose.router.state
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.Current
import models.Daily
import models.Hourly
import models.Location
import models.WeatherAPIResponse
import presentation.navigation.ViewModel

class HomeViewModel() : ViewModel() {
    private val coroutineHandlerException =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            println("error is ${throwable.message}")
        }
    private val viewModelScope =
        CoroutineScope(Dispatchers.Unconfined + SupervisorJob() + coroutineHandlerException)

    private val weatherApi = WeatherAPI()
    private val locationService = LocationService()

    private val _states = MutableStateFlow<HomeState>(HomeState.Loading)
    val states: StateFlow<HomeState> = _states

    init {
        fetchCurrentLocationData()
    }

    private fun fetchCurrentLocationData(){
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocationOneTime()
                withContext(Dispatchers.IO) {
                    val weatherData =
                        weatherApi.getWeatherApiDataFrLonLat(location.latitude, location.longitude)
                    if (weatherData.isSuccess) {
                        _states.emit(
                            HomeState.Success(weatherData.getOrNull(), location)
                        )
                    } else {
                        _states.emit(HomeState.Error(weatherData.exceptionOrNull()?.message ?: "Error API weather"))
                    }
                }
            } catch (e: Exception){
                println("Emitting error state")
                _states.emit(HomeState.Error(e.message ?: "Something went wrong"))
            }
            catch (error: Throwable) {
                _states.emit(HomeState.Error("Something went wrong ${error.message}"))
            }
        }
    }


    fun onAction(actions: HomeViewModelActions) {
        viewModelScope.launch {
            when (actions) {
                is HomeViewModelActions.RefreshCurrentLocation -> {

                }

                is HomeViewModelActions.AddNewPlace -> {

                }
            }
        }
    }
}