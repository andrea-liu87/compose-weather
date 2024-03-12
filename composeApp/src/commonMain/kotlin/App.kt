import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import home.Home
import models.Weather
import theme.WeatherTheme

sealed class BottomSheetScreen {
    data object ListScreen : BottomSheetScreen()
    data class DetailScreen(
        val weather: Weather? = null
    ) : BottomSheetScreen()
    data object None : BottomSheetScreen()
}
@Composable
fun App() {
    WeatherTheme {
        Home()
    }
}

