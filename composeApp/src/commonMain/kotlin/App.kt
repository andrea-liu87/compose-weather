import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import api.WeatherAPI
import home.WeatherView
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun App() {
    val api = WeatherAPI(data.HttpClient)
    MaterialTheme {
        WeatherView(api)
    }
}

