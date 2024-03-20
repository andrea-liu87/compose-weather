package component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import home.convertToC
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.Hourly
import org.jetbrains.compose.resources.painterResource
import theme.SolidBlue

@Composable
fun WeatherBar(label: String, temp:String){
    Column(
        modifier = Modifier
            .height(146.dp)
            .background(color = SolidBlue.copy(0.2f), shape = RoundedCornerShape(percent = 100))
            .border(BorderStroke(2.dp, Color.White.copy(0.2f)), shape = RoundedCornerShape(percent = 100))
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .shadow(elevation = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(modifier = Modifier.padding(top = 8.dp), text = label, style = TextStyle(color = Color.White, fontSize = 20.sp))
        Image( Icons.Filled.ArrowBack, contentDescription = "")
        Text(modifier = Modifier.padding(bottom = 8.dp), text = temp,style = TextStyle(color = androidx.compose.ui.graphics.Color.White, fontSize = 24.sp))
    }
}

@Composable
fun HourForecast(hourList: ArrayList<Hourly>?){
    if (hourList!= null) {
        LazyRow(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(hourList) { hourly ->
                WeatherBar(
                hourly.dt?.let { timeStampToString(it.toLong()) } ?: "__",
                    "${hourly.temp?.let { convertToC(it) }}°")
            }
        }
    } else {
        var time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
        val listS = arrayListOf<Int>()
        for (i in 0..24) {
            time += 1
            if (time == 24){time = 0}
            listS.add(time)
        }
        LazyRow(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(listS) { hourly ->
                WeatherBar(
                     if (hourly > 10){"$hourly"} else {"0$hourly"},
                    "__")
            }
        }
    }
}

fun timeStampToString(netDate: Long): String {
    return try {
        val instant = Instant.fromEpochMilliseconds(netDate)
        val hour = instant.toLocalDateTime(TimeZone.currentSystemDefault()).hour
        if (hour > 10) {
            return hour.toString()
        } else {
            return "0$hour"
        }
    } catch (e: Exception) {
        e.toString()
    }
}