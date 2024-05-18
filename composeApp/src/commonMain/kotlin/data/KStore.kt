package data

import io.github.xxfast.kstore.KStore
import models.WeatherAPIResponse

expect val weatherDataSaved: KStore<WeatherAPIResponse>