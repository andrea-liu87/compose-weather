package presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import compose.icons.WeatherIcons
import compose.icons.weathericons.DaySunny
import compose.icons.weathericons.Fog
import compose.icons.weathericons.Humidity
import compose.icons.weathericons.StrongWind
import compose.icons.weathericons.Sunrise
import compose.icons.weathericons.Thermometer
import compose.icons.weathericons.Windy
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
        Row {
            Image(imageVector = WeatherIcons.DaySunny, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
            Text("  UV Index", color = secondary)
        }
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
        Row {
            Image(imageVector = WeatherIcons.Sunrise, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
            Text("  Sunrise", color = secondary)
        }
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
        Row {
            Image(imageVector = WeatherIcons.Thermometer, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
            Text("  Feels Like", color = secondary, modifier = Modifier.padding(top = 12.dp))
        }
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
        Row {
            Image(imageVector = WeatherIcons.Humidity, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
            Text("  Humidity", color = secondary)
        }
        Text(text = "${state.weatherData?.current?.humidity}%", style = MaterialTheme.typography.h4, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.weight(3f))
    }
}

@Composable
fun Wind(state: HomeState, modifier: Modifier){
    Column(
        modifier.height(widgetSize.dp)
            .padding(4.dp)
            .border(border = borderWidgetStroke, shape = borderWidgetShape)
            .padding(start = 12.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Row {
            Image(imageVector = WeatherIcons.StrongWind, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
            Text("  Wind Speed", color = secondary)
        }
        Text("${state.weatherData?.hourly?.windSpeed?.get(0)?.toInt()} km/h", style = MaterialTheme.typography.h4)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun Visibility(state: HomeState, modifier: Modifier){
    val visibility = state.weatherData?.hourly?.visibility?.get(0)?.toInt()?.div(1000) ?: 0
    Column(
        modifier.height(widgetSize.dp)
            .padding(4.dp)
            .border(borderWidgetStroke, borderWidgetShape)
            .padding(start = 12.dp)
    ) {
        Spacer(Modifier.weight(2f))
        Row {
            Image(imageVector = WeatherIcons.Fog, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
            Text("  Visibility", color = secondary)
        }
        Text(text = "$visibility km", style = MaterialTheme.typography.h4, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.weight(3f))
    }
}