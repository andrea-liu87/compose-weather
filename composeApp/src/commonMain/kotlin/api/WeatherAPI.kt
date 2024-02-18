package api

import com.andreasgift.kmpweatherapp.BuildKonfig.API_KEY
import models.WeatherAPIResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import utils.get


class WeatherAPI(private val client: HttpClient)  {
    private val baseUrl = "https://api.openweathermap.org/data/2.5/weather?"
    private val apiUrl =
        "https://api.openweathermap.org/data/2.5/weather?lat=45.5019&lon=-73.5674&appid=${API_KEY}"

    suspend fun getWeatherApiData(): Result<WeatherAPIResponse> =
        client.get {
            url {
                path("data/2.5/weather") } }

    suspend fun getWeatherApiDataFrLonLat(latitude: Double, longitude:Double): Result<WeatherAPIResponse> =
        client.get { url {
            path("data/2.5/weather?lat=$latitude&lon=$longitude")} }
}