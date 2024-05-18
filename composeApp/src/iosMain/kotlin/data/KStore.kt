package data

import bussiness.di.appStorage
import io.github.xxfast.kstore.KStore
import models.WeatherAPIResponse
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath


actual val weatherDataSaved: KStore<WeatherAPIResponse> by lazy {
    storeOf("$appStorage/saved.json".toPath())
}