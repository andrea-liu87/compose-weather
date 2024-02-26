import models.Location

actual class LocationService actual constructor() {
    actual suspend fun getCurrentLocationOneTime(): Location {
      return Location(0.00 , 0.00)
    }

}