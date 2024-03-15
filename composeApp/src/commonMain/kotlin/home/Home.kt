package home

import MainScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import component.TabBar
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberOnRoute
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import list.ListScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun Home() {
    val router: Router<MainScreen> = rememberRouter(MainScreen::class) { listOf(MainScreen.HomeScreen) }

    RoutedContent(
        router = router,
        animation = predictiveBackAnimation(
            backHandler = LocalRouterContext.current.backHandler,
            onBack = { router.pop() },
            animation = stackAnimation(slide())
        ),
    ) { screen ->
        when (screen) {
            MainScreen.HomeScreen -> MainHomeScreen()
            is MainScreen.ListScreen -> ListScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainHomeScreen() {
    val viewModel: HomeViewModel =
        rememberOnRoute(HomeViewModel::class) { savedState -> HomeViewModel(savedState) }

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
            sheetContent = { BottomSheetContent() },
            //tab row ?? + weatherbar 146 + tab bar 100 + padding 3x20
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
                painterResource("background.png"),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val name = state.weatherData?.name ?: "Montreal"
            val temp = state.weatherData?.main?.temp
            val descr = state.weatherData?.weather?.get(0)?.description ?: ""
            val highest = state.weatherData?.main?.tempMax
            val lowest = state.weatherData?.main?.tempMin

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(name, style = MaterialTheme.typography.h3)
                Text("${convertToC(temp ?: 0.0)}° C", style = MaterialTheme.typography.h3)
                Text(descr, style = MaterialTheme.typography.body1, color = Color.Gray)
                Text("H:${convertToC(highest ?: 0.0)}° L:${convertToC(lowest ?: 0.00)}°", style = MaterialTheme.typography.body1)
            }
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp)
                    .weight(1.5f),
                painter = painterResource("house2.png"),
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
