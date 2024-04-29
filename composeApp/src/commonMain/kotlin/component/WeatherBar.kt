package component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberImagePainter
import home.convertToC
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.Daily
import models.Hourly
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.SolidBlue
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WeatherBar(label: String, temp:String, icon: String){
    val painterResource = rememberImagePainter("https://openweathermap.org/img/wn/$icon@2x.png")

    Column(
        modifier = Modifier
            .height(150.dp)
            .width(60.dp)
            .background(color = SolidBlue.copy(0.2f), shape = RoundedCornerShape(percent = 100))
            .border(BorderStroke(2.dp, Color.White.copy(0.2f)), shape = RoundedCornerShape(percent = 100))
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .shadow(elevation = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(modifier = Modifier.padding(top = 8.dp), text = label, style = TextStyle(color = Color.White, fontSize = 20.sp))
        Image(painterResource, "weather icon", contentScale = ContentScale.Fit)
        Text(modifier = Modifier.padding(bottom = 8.dp), text = temp,style = TextStyle(color = androidx.compose.ui.graphics.Color.White, fontSize = 24.sp))
    }
}

@Composable
fun HourForecast(hourList: ArrayList<Hourly>?){
    if (hourList!= null && hourList.size > 0) {
            LazyRow(
                modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 7.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                itemsIndexed(hourList) { index, hourly ->
                    if (index <= 24) {
                        WeatherBar(
                            label = timeStampToString(hourly.dt!!.toLong()),
                            temp = "${convertToC(hourly.temp ?: 0.00)}°",
                            icon = hourly.weather[0].icon ?: "10n"
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
fun WeeklyForecast(dailyList: ArrayList<Daily>?){
    if (dailyList!= null && dailyList.size > 0) {
        LazyRow(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 7.dp, bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            itemsIndexed(dailyList) { index, daily ->
                if (index <= 7) {
                    WeatherBar(
                        label = timeStampToString(daily.dt!!.toLong(), 1),
                        temp = "${convertToC(daily.temp?.day ?: 0.00)}°",
                        icon = daily.weather[0].icon ?: "10n"
                    )
                    Box(Modifier.width(5.dp))
                }
            }
        }
    } else {
        WeeklyForecastEmptyData()
    }
}

fun getWeatherIcon(description : String): String {
    if (description.contains("wind")) return "icon_windy.png"
    if (description.contains("snow")) return "icon_windy.png"
    if (description.contains("cloud")) return "icon_windy.png"
    return if (description.contains("rain")) "icon_rainy.png"
    else
        "icon_rainy.png"
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