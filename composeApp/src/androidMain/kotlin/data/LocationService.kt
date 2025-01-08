package data

import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.currentLocationOrNull
import dev.jordond.compass.geolocation.mobile
import models.Location
import org.lighthousegames.logging.logging
import java.util.concurrent.atomic.AtomicReference

actual class LocationService actual constructor() {
    // Define an atomic reference to store the latest location
    private val latestLocation = AtomicReference<Location?>(null)

    private val geocoder = Geocoder()
    private val geolocator: Geolocator = Geolocator.mobile()

    actual suspend fun getCurrentLocationOneTime(): Result<Location>  {
        try {
            val location = geolocator.currentLocationOrNull()
            if (location != null) {
                val placeName = geocoder.reverse(location.coordinates)
                val currentLoc = Location(
                    location.coordinates.latitude,
                    location.coordinates.longitude,
                    placeName.getFirstOrNull()?.subLocality
                )
                logging("Location Service").d { "${placeName.getFirstOrNull()?.administrativeArea} " +
                        "${placeName.getFirstOrNull()?.subAdministrativeArea} " +
                        "${placeName.getFirstOrNull()?.locality} " +
                        "${placeName.getFirstOrNull()?.subLocality}" }
                return Result.success(currentLoc)
            } else {
                return Result.failure(Throwable("Error during parsing"))
            }
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

    actual suspend fun getCoordinates(place: String): Location? {
        val location = geolocator.currentLocationOrNull()
        if (location != null) {
            return Location(location.coordinates.latitude, location.coordinates.longitude, place)
        }
        return null
    }

}