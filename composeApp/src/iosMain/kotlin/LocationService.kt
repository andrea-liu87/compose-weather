import co.touchlab.stately.concurrency.AtomicReference
import models.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.lighthousegames.logging.logging
import platform.CoreLocation.CLDeviceOrientationPortrait
import platform.CoreLocation.CLGeocodeCompletionHandler
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLDistanceFilterNone
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLLocationAccuracyBestForNavigation
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class LocationService actual constructor() {
    val log = logging("Location Service")
    // Define a native CLLocationManager object
    private val oneTimeLocationManager = CLLocationManager()

    // Define an atomic reference to store the latest location
    private val latestLocation = AtomicReference<Location?>(null)

    // Define a custom delegate that extends NSObject and implements CLLocationManagerDelegateProtocol
    private class LocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
        val log = logging("Location Service")

        // Define a callback to receive location updates
        var onLocationUpdate: ((Location?) -> Unit)? = null

        @OptIn(ExperimentalForeignApi::class)
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            didUpdateLocations.firstOrNull()?.let {
                val location = it as CLLocation
                location.coordinate.useContents {
                    CLGeocoder().reverseGeocodeLocation(location) { list, error ->
                        if (error == null && list?.get(0) is CLPlacemark) {
                            onLocationUpdate?.invoke(
                                Location(latitude, longitude, (list[0] as CLPlacemark).subAdministrativeArea)
                            )
                        } else {
                            onLocationUpdate?.invoke(Location(latitude, longitude))
                        }
                    }
                }

            }
        }


        override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {

        }

        override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
            log.i{"Error: ${didFailWithError.localizedFailureReason} ${didFailWithError.localizedDescription}, ${didFailWithError.localizedRecoverySuggestion}" }
            log.i {"Error: ${didFailWithError.userInfo["timestamp"]}" }
            onLocationUpdate?.invoke(null)
        }

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: Int) {
            log.i{"Authorization status changed to: $didChangeAuthorizationStatus" }
        }

        override fun locationManagerDidPauseLocationUpdates(manager: CLLocationManager) {
            log.i{"locationManagerDidPauseLocationUpdates" }
        }

        override fun locationManagerDidResumeLocationUpdates(manager: CLLocationManager) {
            log.d{"locationManagerDidResumeLocationUpdates" }
        }
    }

    actual suspend fun getCurrentLocationOneTime(): Location = suspendCoroutine { continuation ->
        oneTimeLocationManager.requestWhenInUseAuthorization()
        oneTimeLocationManager.desiredAccuracy = kCLLocationAccuracyBest

        oneTimeLocationManager.startUpdatingLocation()

        // Define a callback to receive location updates
        val locationDelegate = LocationDelegate()
        locationDelegate.onLocationUpdate = { location ->
            oneTimeLocationManager.stopUpdatingLocation()
            latestLocation.set(location)

            location?.run {
                continuation.resume(this)
            } ?: run {
                continuation.resumeWithException(Exception("Unable to get current location"))
            }
        }
        oneTimeLocationManager.delegate = locationDelegate
    }

}