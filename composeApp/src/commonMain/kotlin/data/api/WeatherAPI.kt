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
    private val baseUrl = "https://api.open-meteo.com/v1/forecast?"
    private val apiUrl =
        "https://api.open-meteo.com/v1/forecast?latitude=24.4105&longitude=54.5359&current=temperature_2m&hourly=temperature_2m&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=auto&forecast_hours=24"

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
                    host = "api.open-meteo.com"
                    protocol = URLProtocol.HTTPS
                    parameters.append("latitude", lat.toString())
                    parameters.append("longitude", lon.toString())
                    parameters.appendAll("current", listOf("temperature_2m","weather_code") )
                    parameters.appendAll("hourly", listOf("temperature_2m","weather_code") )
                    parameters.appendAll(name = "daily", listOf("weather_code","temperature_2m_max","temperature_2m_min"))
                    parameters.append("timezone", "auto")
                    parameters.append("forecast_hours","24")
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
                host = "api.open-meteo.com"
                protocol = URLProtocol.HTTPS
                parameters.append("latitude", "45.5019")
                parameters.append("longitude", "-73.5674")
                parameters.append("current", "temperature_2m" )
                parameters.append("hourly", "temperature_2m" )
                parameters.appendAll(name = "daily", listOf("temperature_2m_max","temperature_2m_min"))
                parameters.append("timezone", "auto")
                parameters.append("forecast_hours","24")
            }
        }
    }


    suspend fun getWeatherApiData(): Result<WeatherAPIResponse> =
        defaultHttpClient.get {
            url {
                path("v1/forecast") } }

    suspend fun getWeatherApiDataFrLonLat(latitude: Double, longitude:Double): Result<WeatherAPIResponse> =
        buildUrl(latitude, longitude).get { url {
            path("v1/forecast")} }
}