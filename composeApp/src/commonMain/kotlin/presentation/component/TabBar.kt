package presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import composeweather.composeapp.generated.resources.Res
import composeweather.composeapp.generated.resources.location
import composeweather.composeapp.generated.resources.mainadd_btn
import composeweather.composeapp.generated.resources.seemore
import org.jetbrains.compose.resources.painterResource
import presentation.home.HomeViewModel
import presentation.home.HomeViewModelActions
import presentation.theme.SolidPurple2

@Composable
fun TabBar(viewModel: HomeViewModel,
           addNewPlace: () -> Unit,
           showListScreen: () -> Unit) {
    val iconSize = 44

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
            .background(color = SolidPurple2.copy(0.5f), shape = BottomBarBackground()),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .clickable { viewModel.onAction(HomeViewModelActions.RefreshCurrentLocation)}
                .height(iconSize.dp)
                .width(iconSize.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit,
            painter = painterResource(Res.drawable.location),
            contentDescription = "location"
        )
        Image(
            modifier = Modifier
                .clickable { addNewPlace()}
                .height(100.dp),
            painter = painterResource(Res.drawable.mainadd_btn),
            contentDescription = "add button"
        )
        Image(
            modifier = Modifier
                .clickable { showListScreen()}
                .height(iconSize.dp)
                .width(iconSize.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(Res.drawable.seemore),
            contentScale = ContentScale.Fit,
            contentDescription = "see more"
        )
    }
}

class BottomBarBackground : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawBackgroundPath(size = size)
        )
    }
}

private fun drawBackgroundPath(size: Size): Path {
    return Path().apply {
        val cornerRadius = 70f
        // Center top arc
        arcTo(
            rect = Rect(
                left = 0f,
                top = -cornerRadius/2,
                right = size.width,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -180.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width, y = size.height )
        lineTo(x = 0f, y = size.height)
        lineTo(x = 0f, y = 0f)

        close()
    }
}