package data

import models.Location

// Define a common class for location service
expect class LocationService() {
    suspend fun getCurrentLocationOneTime(): Result<Location>
    suspend fun getCoordinates(place:String): Location?
}