package api

import com.andreasgift.kmpweatherapp.BuildKonfig.apiKey
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
        "https://api.openweathermap.org/data/2.5/weather?lat=45.5019&lon=-73.5674&appid=${apiKey}"

    suspend fun getWeatherApiData(): Result<WeatherAPIResponse> =
        client.get { url { path("data/2.5/weather?lat=45.5019&lon=-73.5674") } }

    suspend fun getWeatherApiDataFrLonLat(latitude: Double, longitude:Double): Result<WeatherAPIResponse> =
        client.get { url {path("data/2.5/weather?lat=$latitude&lon=$longitude")} }

    @OptIn(DelicateCoroutinesApi::class)
    fun getWeatherAPIData(
        successFunction: (WeatherAPIResponse) -> Unit, failureFunction: (String) -> Unit) {
        GlobalScope.launch() {
            try {
                val url = apiUrl
                val json = HttpClient().get(url) {}
                Json.decodeFromString(WeatherAPIResponse.serializer(), json.bodyAsText())
                    .also(successFunction)
            } catch (ex: Exception) {
                failureFunction("${ex.message}")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getWeatherAPIDataLatLon(
        latitude: Double, longitude:Double,
        successFunction: (WeatherAPIResponse) -> Unit, failureFunction: (String) -> Unit) {
        GlobalScope.launch {
            try {
                val url = "${baseUrl}lat=${latitude}&lon=${longitude}&appid=${apiKey}"
                val json = HttpClient().get(url) {}
                Json.decodeFromString(WeatherAPIResponse.serializer(), json.bodyAsText())
                    .also(successFunction)
            } catch (ex: Exception) {
                failureFunction("${ex.message} KEY: $apiUrl")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getWeatherAPIDataPlace(
        place: String,
        successFunction: (WeatherAPIResponse) -> Unit, failureFunction: (String) -> Unit) {
        GlobalScope.launch {
            try {
                val url = "${baseUrl}q=${place}&appid=${apiKey}"
                val json = HttpClient().get(url) {}
                Json.decodeFromString(WeatherAPIResponse.serializer(), json.bodyAsText())
                    .also(successFunction)
            } catch (ex: Exception) {
                failureFunction("${ex.message} KEY: $apiUrl")
            }
        }
    }
}