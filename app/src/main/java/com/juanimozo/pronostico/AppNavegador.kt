package com.juanimozo.pronostico

interface AppNavegador {
    fun navegarAPronosticoActual(codigoPostal: String)
    fun navegarAUbicacionEntrada()
    fun navegarADetallePronostico(pronostico: PronosticoDiario)
}