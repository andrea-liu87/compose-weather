import models.Location
import android.annotation.SuppressLint
import android.location.Geocoder
import com.andreasgift.composeweather.ApplicationContextInitializer
import com.andreasgift.composeweather.applicationContext
import com.google.android.gms.location.LocationServices
import org.lighthousegames.logging.logging
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class LocationService actual constructor() {
    // Define an atomic reference to store the latest location
    private val latestLocation = AtomicReference<Location?>(null)

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            applicationContext
        )
    }

    private val geocoder = Geocoder(applicationContext)

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocationOneTime(): Location = suspendCoroutine { continuation ->
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { androidOsLocation ->
                val addresses = geocoder.getFromLocation(androidOsLocation.latitude, androidOsLocation.longitude, 1)
                val updatedLocation = Location(
                    androidOsLocation.latitude,
                    androidOsLocation.longitude,
                    addresses?.get(0)?.subAdminArea ?: addresses?.get(0)?.adminArea
                )
                latestLocation.set(updatedLocation)
                continuation.resume(updatedLocation)
            } ?: run {
                continuation.resumeWithException(Exception("Unable to get current location"))
            }
        }.addOnFailureListener { e ->
            continuation.resumeWithException(e)
        }
    }

}