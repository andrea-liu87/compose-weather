package data.api

import com.andreasgift.kmpweatherapp.BuildKonfig.API_KEY
import models.WeatherAPIResponse
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cache.storage.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okio.FileSystem


class WeatherAPI()  {
    private val baseUrl = "https://api.openweathermap.org/data/3.0/onecall?"
    private val apiUrl =
        "https://api.openweathermap.org/data/3.0/onecall?lat=45.5019&lon=-73.5674&exclude=minutely&appid=${API_KEY}"

    private fun buildUrl(lat: Double, lon: Double): HttpClient =
        HttpClient() {
            install(HttpCache)
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.BODY
            }

            defaultRequest {
                url {
                    host = "api.openweathermap.org"
                    protocol = URLProtocol.HTTPS
                    parameters.append("lat", lat.toString())
                    parameters.append("lon", lon.toString())
                    parameters.append("exclude", "minutely" )
                    parameters.append("appid", API_KEY)
                }
            }
        }

    val defaultHttpClient = HttpClient {
        install(HttpCache)

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) }

        install(Logging) { logger = Logger.SIMPLE }

        defaultRequest {
            url {
                host = "api.openweathermap.org"
                protocol = URLProtocol.HTTPS
                parameters.append("lat", "45.5019")
                parameters.append("lon", "-73.5674")
                parameters.append("exclude", "minutely" )
                parameters.append("appid", API_KEY)
            }
        }
    }


    suspend fun getWeatherApiData(): Result<WeatherAPIResponse> =
        defaultHttpClient.get {
            url {
                path("data/3.0/onecall") } }

    suspend fun getWeatherApiDataFrLonLat(latitude: Double, longitude:Double): Result<WeatherAPIResponse> =
        buildUrl(latitude, longitude).get { url {
            path("data/3.0/onecall")} }
}