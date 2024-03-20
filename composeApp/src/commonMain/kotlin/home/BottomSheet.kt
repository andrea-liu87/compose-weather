package home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.HourForecast
import org.lighthousegames.logging.logging
import theme.SolidPurple
import theme.UnselectedTabTitle
import theme.secondary

@Composable
fun BottomSheetContent(state: HomeState) {
    val titles = listOf("Hourly Forecast", "Weekly Forecast")
    var tabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    text = { Text(title, color =
                    if (tabIndex == index) secondary else UnselectedTabTitle
                    ) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    selectedContentColor = secondary,
                    unselectedContentColor = UnselectedTabTitle
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
                Text(text = "Weekly Forecast Page")
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