import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import home.Home
import models.Weather
import theme.WeatherTheme

@Parcelize
sealed class MainScreen : Parcelable {
    data object HomeScreen : MainScreen()
    data object ListScreen : MainScreen()
}
@Composable
fun App() {
    WeatherTheme {
        Home()
    }
}

