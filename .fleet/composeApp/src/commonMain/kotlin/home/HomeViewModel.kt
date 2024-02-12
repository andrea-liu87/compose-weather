package home

import api.WeatherAPI
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import data.HttpClient
import io.github.xxfast.decompose.router.SavedStateHandle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import models.WeatherAPIResponse
import navigation.ViewModel

class HomeViewModel(savedState: SavedStateHandle) : ViewModel() {
    private val eventsFlow: MutableSharedFlow<HomeEvent> = MutableSharedFlow(5)
    private val initialState: HomeState = savedState.get() ?: HomeState(
        WeatherAPIResponse()
    )
    private val webService = WeatherAPI(HttpClient)

    val states: StateFlow<HomeState> by lazy {
        moleculeFlow(RecompositionMode.Immediate) { HomeDomain(initialState, eventsFlow, webService) }
            .onEach { state -> savedState.set(state) }
            .stateIn(this, SharingStarted.Lazily, initialState)
    }

    fun onRefresh() { launch { eventsFlow.emit(HomeEvent.Refresh) } }
}