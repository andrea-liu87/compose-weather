package presentation.home

import MainScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import composeweather.composeapp.generated.resources.Res
import composeweather.composeapp.generated.resources.background
import composeweather.composeapp.generated.resources.house2
import presentation.component.LoadingWidget
import presentation.component.TabBar
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.router.stack.RoutedContent
import io.github.xxfast.decompose.router.stack.Router
import io.github.xxfast.decompose.router.stack.rememberRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.weatherCodeToDescription
import presentation.list.ListScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.component.AddNewPlacesDialog

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun Home() {
    val router: Router<MainScreen> = rememberRouter(MainScreen::class) { listOf(MainScreen.HomeScreen) }
    val animation : StackAnimation<MainScreen, RouterContext> = predictiveBackAnimation(
        fallbackAnimation = stackAnimation(slide()),
        onBack = { router.pop() },
        backHandler = LocalRouterContext.current.backHandler)

    RoutedContent(
        router, animation = animation
    ) { screen ->
        when (screen) {
            MainScreen.HomeScreen -> MainHomeScreen()
            is MainScreen.ListScreen -> ListScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainHomeScreen() {
    val viewModel: HomeViewModel =
        rememberOnRoute(key = "home", type = HomeViewModel::class) { savedState -> HomeViewModel() }

    val state: State<HomeState> = viewModel.states.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    val radius =
        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp
        else 16.dp

    var showNewPlace by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { TabBar(
            viewModel = viewModel,
            addNewPlace = {showNewPlace = true}) }
    ) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = { if (state.value is HomeState.Success)
                BottomSheetContent(
                    state.value as HomeState.Success,
                    bottomSheetScaffoldState.bottomSheetState) },
            //tab row ?? + weatherbar 120 + tab bar 100 + padding 3x20
            sheetPeekHeight = 360.dp,
            sheetShape = RoundedCornerShape(topStart = radius, topEnd = radius),
            sheetBackgroundColor = Color.Black.copy(0.3f),
            content = {
                when (val result = state.value){
                is HomeState.Loading -> LoadingWidget()
                is HomeState.Success -> WeatherView(state = result,
                    bottomSheetScaffoldState.bottomSheetState,
                    showNewPlaceWidget = showNewPlace,
                    closeNewPlaceWidget = {showNewPlace = false})
                    is HomeState.Error ->  {}
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeatherView(
    state: HomeState.Success,
    bottomSheetState: BottomSheetState,
    showNewPlaceWidget: Boolean,
    closeNewPlaceWidget: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(Res.drawable.background),
                contentScale = ContentScale.FillBounds
            )
    ) {
        val name = state. location?.name ?: state.weatherData?.timezone
        val temp = state.weatherData?.current?.temperature2m
        val descr = weatherCodeToDescription(state.weatherData?.current?.weatherCode ?: 0)
        val highest = state.weatherData?.daily?.temperature2mMax
        val lowest = state.weatherData?.daily?.temperature2mMin

        if (bottomSheetState.isExpanded){
            Box(modifier = Modifier.fillMaxHeight(0.2f)) {
                Text(
                    "$name | $temp° C",
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1.5f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(name ?: "Unknown Location", style = MaterialTheme.typography.h3)
                    Text("$temp° C", style = MaterialTheme.typography.h3)
                    Text(descr, style = MaterialTheme.typography.body1, color = Color.Gray)
                    Text("H:${highest?.get(0)}° L:${lowest?.get(0)}°", style = MaterialTheme.typography.body1)
                }
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp)
                        .weight(1.5f),
                    painter = painterResource(Res.drawable.house2),
                    contentDescription = "house",
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        if (showNewPlaceWidget) AddNewPlacesDialog(
            closeDialog = {closeNewPlaceWidget()}
        )
    }
}
