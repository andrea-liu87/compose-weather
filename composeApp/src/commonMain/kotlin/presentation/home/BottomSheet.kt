package presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import presentation.component.HourForecast
import presentation.component.WeeklyForecast
import presentation.theme.SolidPurple
import presentation.theme.secondary
import presentation.theme.widgetBorderColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(state: HomeState, bottomSheetState: BottomSheetState) {
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
                .fillMaxSize(),
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
                    Column(
                        Modifier.height(widgetSize.dp)
                            .padding(4.dp)
                            .weight(1f)
                            .border(
                                border = BorderStroke(2.dp, widgetBorderColor),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(start = 12.dp)
                    ) {
                        Spacer(Modifier.weight(1f))
                        Text("UV Index", color = secondary)
                        Spacer(Modifier.weight(1f))
                        Text("4", style = MaterialTheme.typography.h4)
                        Text("Moderate", style = MaterialTheme.typography.h4)
                        Spacer(Modifier.weight(1f))
                    }
                    Column(
                        Modifier.height(widgetSize.dp)
                            .padding(4.dp)
                            .weight(1f)
                            .border(
                                border = BorderStroke(2.dp, widgetBorderColor),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(start = 12.dp)
                    ) {
                        Spacer(Modifier.weight(1f))
                        Text("Sunrise", color = secondary)
                        Spacer(Modifier.weight(1f))
                        Text("5:28 AM", style = MaterialTheme.typography.h4)
                        Text("Sundown 6:23 PM", color = Color.White)
                        Spacer(Modifier.weight(1f))
                    }
                }
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