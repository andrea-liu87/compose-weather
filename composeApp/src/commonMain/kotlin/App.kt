import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import home.Home
import theme.WeatherTheme

@Composable
fun App() {
    WeatherTheme {
        Home()
    }
}

