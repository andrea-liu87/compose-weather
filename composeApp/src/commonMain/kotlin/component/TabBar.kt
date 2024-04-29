package component

import Platform
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import home.HomeViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.SolidPurple2

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TabBar(viewModel: HomeViewModel) {
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
                .clickable { viewModel.onRefresh()}
                .height(iconSize.dp)
                .width(iconSize.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit,
            painter = painterResource(DrawableResource("location.png")),
            contentDescription = "location"
        )
        Image(
            modifier = Modifier
                .clickable { }
                .height(100.dp),
            painter = painterResource(DrawableResource("mainadd_btn.xml")),
            contentDescription = "add button"
        )
        Image(
            modifier = Modifier
                .clickable { }
                .height(iconSize.dp)
                .width(iconSize.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(DrawableResource("seemore.png")),
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