package home

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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import api.WeatherAPI
import component.TabBar
import io.github.xxfast.decompose.router.rememberOnRoute
import io.ktor.utils.io.core.*
import models.WeatherAPIResponse
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Home() {
    val viewModel: HomeViewModel =
        rememberOnRoute(HomeViewModel::class) { savedState -> HomeViewModel(savedState) }

    val state: HomeState by viewModel.states.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()
    val radius =
        if (sheetState.bottomSheetState.isExpanded) 0.dp
        else 16.dp

    Scaffold(
        bottomBar = { TabBar() }
    ) {
        BottomSheetScaffold(
            sheetContent = { BottomSheetContent() },
            //sheetPeekHeight = LocalWindowInfo.current.containerSize.height.dp/3,
            sheetPeekHeight = 600.dp,
            sheetShape = RoundedCornerShape(topStart = radius, topEnd = radius),
            sheetBackgroundColor = Color.Black.copy(0.3f),
            content = {
                WeatherView(state.weatherData ?: WeatherAPIResponse())
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WeatherView(weather: WeatherAPIResponse) {
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
            val name by remember { mutableStateOf(weather.name ?: "Montreal") }
            val temp by remember { mutableStateOf(weather.main.temp) }
            val descr by remember { mutableStateOf(weather.weather[0].description ?: "") }
            val highest by remember { mutableStateOf(weather.main.tempMax) }
            val lowest by remember { mutableStateOf(weather.main.tempMin) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(name, style = MaterialTheme.typography.h3)
                Text("$temp° C", style = MaterialTheme.typography.h2)
                Text(descr, style = MaterialTheme.typography.body1, color = Color.Gray)
                Text("H:$highest° L:$lowest°", style = MaterialTheme.typography.body1)
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
