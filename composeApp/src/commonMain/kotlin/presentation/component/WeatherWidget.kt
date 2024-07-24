package presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import presentation.home.HomeState
import presentation.theme.secondary
import presentation.theme.widgetBorderColor

val widgetSize = 196
val borderWidgetStroke = BorderStroke(2.dp, widgetBorderColor)
val borderWidgetShape = RoundedCornerShape(16.dp)

@Composable
fun UVIndex(state: HomeState, modifier: Modifier){
    Column(
        modifier.height(widgetSize.dp)
            .padding(4.dp)
            .border(borderWidgetStroke, borderWidgetShape)
            .padding(start = 12.dp)
    ) {
        val  uvIndex = state.weatherData?.daily?.uvIndex?.get(0) ?: 0.0
        val level = if ( uvIndex <= 2.0){"Low"}
        else if (uvIndex >= 8){"High"} else {"Moderate"}

        Spacer(Modifier.weight(1f))
        Text("UV Index", color = secondary)
        Spacer(Modifier.weight(1f))
        Text(text = uvIndex.toString(), style = MaterialTheme.typography.h4)
        Text(level, style = MaterialTheme.typography.h4)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun Sunrise(state: HomeState, modifier: Modifier){
    Column(
        modifier.height(widgetSize.dp)
            .padding(4.dp)
            .border(border = borderWidgetStroke, shape = borderWidgetShape)
            .padding(start = 12.dp)
    ) {
        val sunrise = LocalDateTime.parse(state.weatherData?.daily?.sunrise!![0]).time
        val sunset = LocalDateTime.parse(state.weatherData.daily?.sunset!![0]).time

        Spacer(Modifier.weight(1f))
        Text("Sunrise", color = secondary)
        Spacer(Modifier.weight(1f))
        Text("${sunrise} AM", style = MaterialTheme.typography.h4)
        Text("Sundown ${sunset} PM", color = Color.White)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun FeelsLike(state: HomeState, modifier: Modifier){
    Column(
        modifier.height(widgetSize.dp)
            .padding(4.dp)
            .border(borderWidgetStroke, borderWidgetShape)
            .padding(start = 12.dp)
    ) {
        Spacer(Modifier.weight(2f))
        Text("Feels Like", color = secondary, modifier = Modifier.padding(top = 12.dp))
        Text(text = "${state.weatherData?.current?.feelsLike}° C", style = MaterialTheme.typography.h4, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.weight(3f))
    }
}

@Composable
fun Humidity(state: HomeState, modifier: Modifier){
    Column(
        modifier.height(widgetSize.dp)
            .padding(4.dp)
            .border(borderWidgetStroke, borderWidgetShape)
            .padding(start = 12.dp)
    ) {
        Spacer(Modifier.weight(2f))
        Text("Humidity", color = secondary)
        Text(text = "${state.weatherData?.current?.humidity}%", style = MaterialTheme.typography.h4, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.weight(3f))
    }
}