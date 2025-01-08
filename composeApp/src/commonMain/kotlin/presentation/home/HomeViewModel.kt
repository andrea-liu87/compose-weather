package presentation.home

import data.LocationService
import data.api.WeatherAPI
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                val location = locationService.getCurrentLocationOneTime().getOrNull()
                withContext(Dispatchers.IO) {
                    val weatherData =
                        weatherApi.getWeatherApiDataFrLonLat(location!!.latitude, location.longitude)
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