import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import api.WeatherAPI
import com.andreasgift.kmpweatherapp.android.ui.home.WeatherView
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val api = WeatherAPI()
    MaterialTheme {
        WeatherView(api)
    }
}

