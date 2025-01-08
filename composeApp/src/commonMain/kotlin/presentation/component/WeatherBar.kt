package presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.WeatherIcons
import compose.icons.weathericons.DayCloudy
import compose.icons.weathericons.DayHaze
import compose.icons.weathericons.DayShowers
import compose.icons.weathericons.DaySnow
import compose.icons.weathericons.DaySunny
import compose.icons.weathericons.DayThunderstorm
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.Daily
import models.Hourly
import models.weatherCodeToDescription
import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.theme.SolidBlue
import presentation.theme.widgetBorderColor
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WeatherBar(label: String, temp:String, icon: ImageVector){
    Column(
        modifier = Modifier
            .height(150.dp)
            .background(color = SolidBlue.copy(0.2f), shape = RoundedCornerShape(percent = 100))
            .border(BorderStroke(2.dp, widgetBorderColor), shape = RoundedCornerShape(percent = 100))
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .shadow(elevation = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(modifier = Modifier.padding(top = 8.dp), text = label, style = TextStyle(color = Color.White, fontSize = 20.sp))
        Image(imageVector = icon, "weather icon", contentScale = ContentScale.Fit, colorFilter = ColorFilter.tint(Color.White))
        Text(modifier = Modifier.padding(bottom = 8.dp), text = temp,style = TextStyle(color = androidx.compose.ui.graphics.Color.White, fontSize = 24.sp))
    }
}

@Composable
fun HourForecast(hourList: Hourly?){
    if (hourList!= null && hourList.temperature2m.size > 0) {
            LazyRow(
                modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 7.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                itemsIndexed(hourList.temperature2m) { index, hourly ->
                    if (index <= 24) {
                        WeatherBar(
                            label = LocalDateTime.parse(hourList.time[index]).time.toString(),
                            temp = "${hourly.toInt()}°",
                            icon =  getWeatherIcon(weatherCodeToDescription(hourList.weatherCode[index]))
                        )
                        Box(Modifier.width(5.dp))
                    }
                }
            }
    } else {
        HourForecastEmptyData()
    }
}

@Composable
fun WeeklyForecast(dailyList: Daily?){
    if (dailyList!= null && dailyList.temperature2mMax.size > 0) {
        LazyRow(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 7.dp, bottom = 16.dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(dailyList.temperature2mMax) { index, daily ->
                if (index <= 7) {
                    val date = LocalDate.parse(dailyList.time[index]).dayOfMonth
                    val maxTemp = dailyList.temperature2mMax[index]
                    WeatherBar(
                        label = if (date < 10) "0$date" else "$date",
                        temp = "${maxTemp.toInt()}°",
                        icon =  getWeatherIcon(weatherCodeToDescription(dailyList.weatherCode[index]))
                    )
                    Box(Modifier.width(5.dp))
                }
            }
        }
    } else {
        WeeklyForecastEmptyData()
    }
}

fun getWeatherIcon(description : String): ImageVector {
    if (description.lowercase().contains("thunderstorm")) return WeatherIcons.DayThunderstorm
    if (description.lowercase().contains("clear")) return WeatherIcons.DaySunny
    if (description.lowercase().contains("rain")) return WeatherIcons.DayShowers
    if (description.lowercase().contains("drizzle")) return WeatherIcons.DayShowers
    if (description.lowercase().contains("snow")) return WeatherIcons.DaySnow
    if (description.lowercase().contains("cloudy")) return WeatherIcons.DayCloudy
    if (description.lowercase().contains("overcast")) return WeatherIcons.DayCloudy
    if (description.lowercase().contains("fog")) return WeatherIcons.DayHaze
    else
        return WeatherIcons.DaySunny
}

@Composable
fun HourForecastEmptyData(){
    val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    val listS = arrayListOf<Int>()
    for (i in 0..24) {
        if (time + i < 24) {
            listS.add(time + i)
        } else {
            listS.add(time + i - 24)
        }
    }
    LazyRow(
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 7.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        items(listS) { hourly ->
            WeatherBar(
                if (hourly >= 10){"$hourly"} else {"0$hourly"},
                "__",
                getWeatherIcon("clear sky")
            )
            Box(Modifier.width(5.dp))
        }
    }
}

@Composable
fun WeeklyForecastEmptyData(){
    val time = Clock.System.now()
    val listS = arrayListOf<Int>()
    for (i in 0..7) {
        val day = time.plus(i.days).toLocalDateTime(TimeZone.currentSystemDefault()).dayOfMonth
        listS.add(day)
    }
    LazyRow(
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 7.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(listS) { weekly ->
            WeatherBar(
                if (weekly >= 10){"$weekly"} else {"0$weekly"},
                "__",
                getWeatherIcon("clear sky")
            )
            Box(Modifier.width(5.dp))
        }
    }
}

fun timeStampToString(netDate: Long, mode: Int = 0): String {
    val instant = Instant.fromEpochSeconds(netDate)
    if (mode == 1){
        return try {
            val day = instant.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfMonth
            if (day >= 10) {
                return day.toString()
            } else {
                return "0$day"
            }
        } catch (e: Exception) {
            e.toString()
        }
    } else {
        return try {
            val hour = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time.hour
            if (hour >= 10) {
                return hour.toString()
            } else {
                return "0$hour"
            }
        } catch (e: Exception) {
            e.toString()
        }
    }
}