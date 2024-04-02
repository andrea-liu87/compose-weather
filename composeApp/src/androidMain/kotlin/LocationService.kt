import models.Location
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.andreasgift.composeweather.ApplicationContextInitializer
import com.andreasgift.composeweather.applicationContext
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
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

    actual suspend fun getCurrentLocationOneTime(): Location = suspendCoroutine { continuation ->
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                        CancellationTokenSource().token

                    override fun isCancellationRequested(): Boolean = false
                })
                .addOnSuccessListener { androidOsLocation ->
                    val addresses = geocoder.getFromLocation(
                        androidOsLocation.latitude,
                        androidOsLocation.longitude,
                        1
                    )
                    logging("location tag").d {
                        androidOsLocation.toString() + " " +
                                addresses?.get(0)?.subAdminArea + addresses?.get(0)?.adminArea
                    }
                    val updatedLocation = Location(
                        androidOsLocation.latitude,
                        androidOsLocation.longitude,
                        addresses?.get(0)?.subAdminArea ?: addresses?.get(0)?.adminArea
                    )
                    latestLocation.set(updatedLocation)
                    continuation.resume(updatedLocation)
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        } else {
            continuation.resumeWithException(Throwable("Location permission is denied"))
        }
    }

}