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
import theme.SolidBlue

@Composable
fun WeatherBar(){
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
        Text(modifier = Modifier.padding(top = 8.dp), text = "12 AM", style = TextStyle(color = Color.White, fontSize = 20.sp))
        Image( Icons.Filled.ArrowBack, contentDescription = "")
        Text(modifier = Modifier.padding(bottom = 8.dp), text ="19°",style = TextStyle(color = androidx.compose.ui.graphics.Color.White, fontSize = 24.sp))
    }
}

@Composable
fun HourForecast(){
    LazyRow(
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(24) { weatherData ->
            WeatherBar()
        }
    }
}