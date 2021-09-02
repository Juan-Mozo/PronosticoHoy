package com.juanimozo.pronostico

fun formatoTemperatura(temp: Float, unidadTemperatura: UnidadTemperatura): String {
    return when(unidadTemperatura) {
        UnidadTemperatura.Fahreinheit -> {
            val temp = (temp + 32f)
            String.format("%.1f°", temp)
        }
        UnidadTemperatura.Celsius -> String.format("%.1f°", temp)
    }
}