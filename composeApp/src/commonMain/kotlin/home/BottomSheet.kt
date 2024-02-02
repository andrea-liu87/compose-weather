package com.andreasgift.kmpweatherapp.android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreasgift.kmpweatherapp.android.ui.component.HourForecast
import com.andreasgift.kmpweatherapp.android.ui.theme.SolidPurple
import com.andreasgift.kmpweatherapp.android.ui.theme.UnselectedTabTitle
import com.andreasgift.kmpweatherapp.android.ui.theme.secondary

@Composable
fun BottomSheetContent() {
    val titles = listOf("Hourly Forecast", "Weekly Forecast")
    var tabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.3f))
            .padding(bottom = 40.dp),
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
                    if (tabIndex == index) secondary else UnselectedTabTitle) },
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
                HourForecast()
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

@Preview
@Composable
fun handleShape(){
    HandleDrag()
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun BottomSheetPreview(){
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    BottomSheetContent()
}