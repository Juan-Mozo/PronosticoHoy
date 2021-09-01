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
        val valoresRandom = List(7) { Random.nextFloat().rem(30) * 30 }
        val itemsPronostico = valoresRandom.map { temp ->
            PronosticoDiario(temp, "Soleado")
        }
        // Envia el valor al MutableLiveData
        _pronosticoSemanal.setValue(itemsPronostico)
    }
}