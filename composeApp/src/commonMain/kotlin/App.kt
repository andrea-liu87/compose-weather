import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

import presentation.home.Home
import presentation.theme.WeatherTheme

@Serializable
sealed class MainScreen  {
    @Serializable
    object HomeScreen : MainScreen()
    @Serializable
    object ListScreen : MainScreen()
}
@Composable
fun App() {
    WeatherTheme {
        Home()
    }
}

