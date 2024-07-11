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
import presentation.list.ListScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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
        rememberOnRoute(key = "home", type = HomeViewModel::class) { savedState -> HomeViewModel(savedState) }

    val state: HomeState by viewModel.states.collectAsState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val radius =
        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 0.dp
        else 16.dp
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        bottomBar = { TabBar(viewModel) }
    ) {
        BottomSheetScaffold(
            sheetContent = { BottomSheetContent(state) },
            //tab row ?? + weatherbar 120 + tab bar 100 + padding 3x20
            sheetPeekHeight = 360.dp,
            sheetShape = RoundedCornerShape(topStart = radius, topEnd = radius),
            sheetBackgroundColor = Color.Black.copy(0.3f),
            content = {
                WeatherView(state = state)
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun openBottomSheet(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {
    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WeatherView(state: HomeState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(Res.drawable.background),
                contentScale = ContentScale.FillBounds
            )
    ) {
        if (state.weatherData == Loading) { LoadingWidget() }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val name = state.location?.name ?: state.weatherData?.timezone
            val temp = state.weatherData?.current?.temp
            val descr = state.weatherData?.current?.weather?.get(0)?.description ?: ""
            val highest = state.weatherData?.daily?.get(0)?.temp?.max
            val lowest = state.weatherData?.daily?.get(0)?.temp?.min

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(name ?: "Unknown Location", style = MaterialTheme.typography.h3)
                Text("${convertToC(temp ?: 0.0)}° C", style = MaterialTheme.typography.h3)
                Text(descr, style = MaterialTheme.typography.body1, color = Color.Gray)
                Text("H:${convertToC(highest ?: 0.0)}° L:${convertToC(lowest ?: 0.00)}°", style = MaterialTheme.typography.body1)
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
}

fun convertToC(f: Double): String {
    val tempInC = f - 273
    return tempInC.toInt().toString()
}
