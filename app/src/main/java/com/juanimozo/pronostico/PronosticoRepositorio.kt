package com.juanimozo.pronostico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class PronosticoRepositorio {

    // Actualiza la data y crea una lista de 7 días de pronóstico
    private val _pronosticoSemanal = MutableLiveData<List<PronosticoDiario>>()

    // Permite que la activity lea los cambios pero no puede modificar la data al ser Live data y no MutablLiveData
    val pronosticoSemanal: LiveData<List<PronosticoDiario>> = _pronosticoSemanal

    // Crea una lista de 7 valores random para simular el pronostico
    fun cargarPronostico(codigoPostal: String) {
        val valoresRandom = List(10) { Random.nextFloat().rem(40) * 40 }
        val itemsPronostico = valoresRandom.map { temp ->
            PronosticoDiario(temp, obtenerDescripcion(temp))
        }
        // Envia el valor al MutableLiveData
        _pronosticoSemanal.setValue(itemsPronostico)
    }

    // Generador de descripción según la temperatura
    private fun obtenerDescripcion(temp: Float) : String {
        return when (temp) {
            in Float.MIN_VALUE.rangeTo(0f) -> "No debería existir"
            in 0f.rangeTo(10f) -> "Frío"
            in 10f.rangeTo(20f) -> "Templado"
            in 20f.rangeTo(30f) -> "Calor"
            in 30f.rangeTo(Float.MAX_VALUE) -> "Ola de Calor"
            else -> "Error"
        }
    }

}