package com.andreasgift.kmpweatherapp.android.ui.home

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreasgift.kmpweatherapp.WeatherAPI
import com.andreasgift.kmpweatherapp.android.R
import com.andreasgift.kmpweatherapp.android.ui.component.TabBar
import com.andreasgift.kmpweatherapp.android.ui.theme.WeatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
    onNavigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    api: WeatherAPI
) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()
    val radius =
        if (sheetState.bottomSheetState.isExpanded) 0.dp
        else 16.dp

    Scaffold(
        bottomBar = { TabBar()}
    ) {
        BottomSheetScaffold(
            sheetContent = { BottomSheetContent() },
            sheetPeekHeight = LocalConfiguration.current.screenHeightDp.dp / 2,
            sheetShape = RoundedCornerShape(topStart = radius, topEnd = radius),
            sheetBackgroundColor = Color.Black.copy(0.3f),
            content = {
                WeatherView(api = api)
            }
        )
    }
}

@Composable
fun WeatherView(api: WeatherAPI) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var name by remember { mutableStateOf("Montreal") }
            var temp by remember { mutableStateOf("19") }
            var descr by remember { mutableStateOf("Clear Sky") }
            var highest by remember { mutableStateOf("23") }
            var lowest by remember { mutableStateOf("12") }

            api.getWeatherAPIData(
                {
                    name = it.name ?: ""
                    temp = convertToC(it.main.temp ?: 0.00)
                    descr = it.weather[0].description ?: ""
                    highest = convertToC(it.main.tempMax ?: 0.00)
                    lowest = convertToC(it.main.tempMin ?: 0.00)
                },
                {
                    name = it
                    temp = ""
                    descr = ""
                }
            )

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
                painter = painterResource(id = R.drawable.house2),
                contentDescription = "house",
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun convertToC (f:Double) : String {
    val tempInC = f - 273
    return String.format("%.0f", tempInC)
}

@Preview(showSystemUi = true)
@Composable
fun WeatherViewPreview(){
    WeatherTheme() {
        WeatherView(api = WeatherAPI())
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview(){
    WeatherTheme() {
        Home(onNavigateToRoute = {}, api = WeatherAPI())
    }
}
