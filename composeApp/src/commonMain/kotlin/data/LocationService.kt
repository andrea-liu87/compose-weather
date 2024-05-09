package data

import models.Location

// Define a common class for location service
expect class LocationService() {
    suspend fun getCurrentLocationOneTime(): Location
}