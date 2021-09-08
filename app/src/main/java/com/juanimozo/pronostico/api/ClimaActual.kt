package com.juanimozo.pronostico.api

import com.squareup.moshi.Json
import retrofit2.http.Field

data class Pronostico(val temp: Float)
data class Coordenadas(val lat: Float, val lon: Float)

data class ClimaActual(
    val name: String,
    val coord: Coordenadas,
    val main: Pronostico
)