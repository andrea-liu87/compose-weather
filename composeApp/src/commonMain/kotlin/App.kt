import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable
import presentation.home.Home
import presentation.theme.WeatherTheme

@Serializable
sealed class MainScreen  {
    data object HomeScreen : MainScreen()
    data object ListScreen : MainScreen()
}
@Composable
fun App() {
    WeatherTheme {
        Home()
    }
}

