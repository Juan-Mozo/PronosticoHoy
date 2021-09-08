package com.juanimozo.pronostico.api

import com.squareup.moshi.Json

data class DescripcionClima(
        val main: String,
        val description: String,
        val icon: String)

data class Temp(
        val min: Float,
        val max: Float
        )

data class PronosticoDiario(
        val dt: Long,
        val temp: Temp,
        val weather: List<DescripcionClima>
)

data class ClimaSemanal(
        val daily: List<PronosticoDiario>
        )