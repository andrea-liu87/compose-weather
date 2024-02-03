package component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.andreasgift.kmpweatherapp.android.R
import theme.SolidPurple2

@Composable
fun TabBar() {
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
                .clickable { }
                .height(iconSize.dp)
                .width(iconSize.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.location),
            contentDescription = "location"
        )
        Image(
            modifier = Modifier
                .clickable { }
                .height(100.dp),
            painter = painterResource(id = R.drawable.mainadd_btn),
            contentDescription = "add button"
        )
        Image(
            modifier = Modifier
                .clickable { }
                .height(iconSize.dp)
                .width(iconSize.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.seemore),
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

@Preview
@Composable
fun BottomBarPreview(){
    TabBar()
}