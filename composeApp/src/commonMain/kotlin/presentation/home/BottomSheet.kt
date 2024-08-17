package presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.http.CacheControl
import kotlinx.datetime.LocalDateTime
import presentation.component.FeelsLike
import presentation.component.HourForecast
import presentation.component.Humidity
import presentation.component.Sunrise
import presentation.component.UVIndex
import presentation.component.Visibility
import presentation.component.WeeklyForecast
import presentation.component.Wind
import presentation.theme.SolidPurple
import presentation.theme.secondary
import presentation.theme.widgetBorderColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(state: HomeState.Success, bottomSheetState: BottomSheetState) {
    val titles = listOf("Hourly Forecast", "Weekly Forecast")
    var tabIndex by remember { mutableStateOf(0) }
    val widgetSize = 196

    Column(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .fillMaxWidth()
            .background(Color.Black.copy(0.3f))
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HandleDrag()
        TabRow(selectedTabIndex = tabIndex,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            indicator = {tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[tabIndex])
                        .height(4.dp)
                        .background(color = SolidPurple, shape = RoundedCornerShape(8.dp))
                )},
            divider = { TabRowDefaults.Divider(color = Color.White) }) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = secondary) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = secondary,
                    unselectedContentColor = secondary
                )
            }
        }
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(1f,false),
            verticalArrangement = Arrangement.Top
        ) {
            if (tabIndex == 0){
                HourForecast(state.weatherData?.hourly)
            } else {
                WeeklyForecast(state.weatherData?.daily)
            }
            if (!bottomSheetState.isCollapsed) {
                Row(
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    UVIndex(state, Modifier.weight(1f))
                    Sunrise(state, Modifier.weight(1f))
                }
                Row(
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    FeelsLike(state, Modifier.weight(1f))
                    Humidity(state, Modifier.weight(1f))
                }
                Row(
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Wind(state, Modifier.weight(1f))
                    Visibility(state, Modifier.weight(1f))
                }
                Box(Modifier.height((widgetSize/2).dp))
            }
        }
    }
}

@Composable
fun HandleDrag(){
    Box(
        modifier = Modifier
            .height(5.dp)
            .width(48.dp)
            .padding(top = 12.dp, bottom = 16.dp)
            .background(color = Color.Black, shape = RoundedCornerShape( 2.dp))
    ) {}
}