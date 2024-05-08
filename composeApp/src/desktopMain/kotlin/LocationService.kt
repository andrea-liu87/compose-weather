import com.andreasgift.kmpweatherapp.BuildKonfig
import com.andreasgift.kmpweatherapp.BuildKonfig.GOOGLE_MAPS_KEY
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import models.GeocodingResponse
import models.Location
import models.LocationRequest
import models.LocationResponse
import models.WeatherAPIResponse
import org.lighthousegames.logging.logging
import utils.get
import java.net.InetAddress


actual class LocationService actual constructor() {

    private val urlPost =
        "https://www.googleapis.com/geolocation/v1/geolocate?key=${GOOGLE_MAPS_KEY}"

    private fun buildUrlGeocoding(lat: Double, lon: Double): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) { logger = Logger.SIMPLE }

            defaultRequest {
                url {
                    host = "maps.googleapis.com"
                    protocol = URLProtocol.HTTPS
                    parameters.append("latlng", "$lat,$lon")
                    parameters.append("key", BuildKonfig.GOOGLE_MAPS_KEY)
                }
            }
        }

    private val client: HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) { logger = Logger.SIMPLE }
        }
    actual suspend fun getCurrentLocationOneTime(): Location = withContext(Dispatchers.IO) {
        val result = requestCurrentLocation().body<LocationResponse>()
        var address = ""
        withContext(Dispatchers.IO) {
            val geocodingResult = parseGeocoding(result.location!!.latitude, result.location!!.longitude)
                .body<GeocodingResponse>().results
            if (geocodingResult.size > 0 && geocodingResult[0].addressComponents.size > 0){
                for (i in 1..3 ){
                    if (i <= geocodingResult[0].addressComponents.size) {
                        address = address + geocodingResult[0].addressComponents[i].shortName + ", "
                    }
                }
            }
            return@withContext Location(result.location!!.latitude, result.location!!.longitude, address)
        }
    }

    private suspend fun requestCurrentLocation(): io.ktor.client.statement.HttpResponse =
        client.post {
            url(urlPost)
            contentType(ContentType.Application.Json)
            setBody(
                LocationRequest(
                    macAddress = InetAddress.getLocalHost().hostAddress,
                    signalStrength = -35,
                    signalToNoiseRatio = 0,
                    channel = 11,
                    age = 0
                )
            )
        }

    private suspend fun parseGeocoding(
        lat: Double,
        lon: Double
    ): io.ktor.client.statement.HttpResponse =
        buildUrlGeocoding(lat, lon).get {
            url {
                path("maps/api/geocode/json")
            }
        }
}

