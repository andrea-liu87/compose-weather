import models.Location
import android.annotation.SuppressLint
import com.andreasgift.composeweather.ApplicationContextInitializer
import com.andreasgift.composeweather.applicationContext
import com.google.android.gms.location.LocationServices
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

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocationOneTime(): Location = suspendCoroutine { continuation ->
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { androidOsLocation ->
                val updatedLocation = Location(androidOsLocation.latitude, androidOsLocation.longitude)
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