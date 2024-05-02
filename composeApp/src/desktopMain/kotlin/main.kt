import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.defaultRouterContext

fun main() {
    application {
        val windowState: WindowState = rememberWindowState()
        val rootRouterContext: RouterContext = defaultRouterContext(windowState = windowState)


        Window(
            title = "Weather Compose",
            state = windowState,
            onCloseRequest = { exitApplication() }
        ) {
            CompositionLocalProvider(
                LocalRouterContext provides rootRouterContext
            ) {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}