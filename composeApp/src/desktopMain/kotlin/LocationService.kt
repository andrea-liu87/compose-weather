import com.andreasgift.kmpweatherapp.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import models.Location
import models.LocationRequest
import models.LocationResponse
import models.WeatherAPIResponse
import utils.get
import java.net.InetAddress
import java.net.http.HttpResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class LocationService actual constructor() {
    private val urlPost =
        "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyAKHKmr7-OiEFtyk1FqUb-DoJkxs6ag8tc"


//    {
//        "macAddress": "9c:1c:12:b0:45:f1",
//        "signalStrength": -43,
//        "signalToNoiseRatio": 0,
//        "channel": 11,
//        "age": 0
//    }

    private val client: HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) { logger = Logger.SIMPLE }
        }
    actual suspend fun getCurrentLocationOneTime(): Location =
        withContext(Dispatchers.IO){
            val response = requestCurrentLocation()
            val result : LocationResponse = response.body<LocationResponse>()
            if (result.location != null ) {
                return@withContext Location(result.location?.latitude ?: 0.00, result.location?.longitude ?: 0.00)
            } else {
                return@withContext Location(0.00 , 0.00)
            }
        }


    private suspend fun requestCurrentLocation(): io.ktor.client.statement.HttpResponse =
        client.post {
            url(urlPost)
            contentType(ContentType.Application.Json)
            setBody(
                LocationRequest(
                    macAddress = InetAddress.getLocalHost().hostAddress,
                    signalStrength = -43,
                    signalToNoiseRatio = 0,
                    channel = 11,
                    age = 0
                )
            )
        }


}

