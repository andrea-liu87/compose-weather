package preview

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import models.Current
import models.Daily
import models.Hourly
import models.Location
import models.WeatherAPIResponse
import presentation.home.BottomSheetContent
import presentation.home.HomeState
import presentation.theme.WeatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun PreviewBottomSheet() {
    val initialHomeState = HomeState(
        WeatherAPIResponse(
            timezone =  "America",
            current = Current(time = "2024-07-14T20:45", interval = 900, temperature2m = 33.7, weatherCode = 77),
            daily = Daily(time = arrayListOf("2024-07-15"), temperature2mMax = arrayListOf(40.0), arrayListOf(32.3), weatherCode = arrayListOf(1)),
            hourly = Hourly(time = arrayListOf("2024-07-15T09:00"), temperature2m = arrayListOf(40.0), weatherCode = arrayListOf(1))
        ),
        Location(
            latitude = 45.5019,
            longitude = -73.5674,
            name = "Montreal"
        )
    )
    WeatherTheme {
        BottomSheetContent(initialHomeState, BottomSheetState(BottomSheetValue.Collapsed))
    }
}