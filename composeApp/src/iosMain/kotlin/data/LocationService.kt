package data

import co.touchlab.stately.concurrency.AtomicReference
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import models.Location
import org.lighthousegames.logging.logging
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class LocationService actual constructor() {
    val log = logging("Location Service")

    private val geocoder = CLGeocoder()
    private val geolocator: Geolocator = Geolocator.mobile()

    // Define an atomic reference to store the latest location
    private val latestLocation = AtomicReference<Location?>(null)


    actual suspend fun getCurrentLocationOneTime(): Result<Location> {
        val result: GeolocatorResult = geolocator.current()

        return suspendCoroutine { continuation ->
            when (result) {
                is GeolocatorResult.Success -> {
                    var updatedLocation = Location(
                        result.data.coordinates.latitude,
                        result.data.coordinates.longitude)
                    geocoder.reverseGeocodeLocation(
                        CLLocation(
                            result.data.coordinates.latitude,
                            result.data.coordinates.longitude
                        ),
                        { list, error ->
                            if (error == null && list?.get(0) is CLPlacemark) {
                                updatedLocation = Location(
                                    result.data.coordinates.latitude,
                                    result.data.coordinates.longitude,
                                    (list[0] as CLPlacemark).subAdministrativeArea
                                )
                            }
                            log.d { "${updatedLocation.name} ${updatedLocation.latitude},${updatedLocation.longitude}" }
                            latestLocation.set(updatedLocation)
                            continuation.resume(Result.success(updatedLocation))
                        }
                    )
                }

                is GeolocatorResult.Error -> continuation.resumeWithException(Exception(result.message))
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getCoordinates(place: String): Location? = suspendCoroutine { continuation ->
        CLGeocoder().geocodeAddressString(place){ list, error ->
            if (error == null && list?.get(0) is CLPlacemark) {
                val location =
                    Location(
                        (list[0] as CLPlacemark).location?.coordinate!!.useContents { latitude },
                        (list[0] as CLPlacemark).location?.coordinate!!.useContents { longitude },
                        (list[0] as CLPlacemark).subAdministrativeArea
                    )
                location.run { continuation.resume(this) }
            } else {run {
                continuation.resumeWithException(Exception("Unable to get current location"))
            }
            }
        }
    }

}