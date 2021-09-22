package com.juanimozo.pronostico.api

import com.squareup.moshi.Json
import retrofit2.http.Field

data class Pronostico(val temp: Float, val pressure: Int, val humidity: Int)
data class InfoSistema(val country: String)
//data class Descripcion(val main: String, val description: String)
// Coordenadas para la call del PronosticoSemanal
data class Coordenadas(val lat: Float, val lon: Float)

data class ClimaActual(
    val name: String,
    val coord: Coordenadas,
    val main: Pronostico,
    val sys: InfoSistema,
//    val weather: Descripcion
)