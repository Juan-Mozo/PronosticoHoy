package com.juanimozo.pronostico

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Ubicacion {
    data class Ciudad(val ciudad: String) : Ubicacion()
}

// Crear la llave como constante para evitar errores
private const val KEY_CIUDAD = "key_ciudad"

// Usar SharedPreferences para almacenar el codigo postal en el repositorio
class UbicacionRepositorio(context: Context) {
    private val preferencias = context.getSharedPreferences("configuracion", Context.MODE_PRIVATE)

    // Actualizar la data
    private val _ubicacionGuardada: MutableLiveData<Ubicacion> = MutableLiveData()
    // Exportar la data actualizada
    val ubicacionGuardada: LiveData<Ubicacion> = _ubicacionGuardada

    // Cuando se crea UbicacionRepositorio se va a iniciar el bloque init
    init {
        // Listener para el sharedPreferences
        preferencias.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            // Si la llave no es igual va a ignorarlo
            if(key != KEY_CIUDAD) return@registerOnSharedPreferenceChangeListener
            transmisionCPGuardado()
        }

        transmisionCPGuardado()
    }

    fun guardarUbicacion(ubicacion: Ubicacion) {
        when (ubicacion) {
            // Modificar SharedPreferences con el codigo postal ingresado
            is Ubicacion.Ciudad -> preferencias.edit().putString(KEY_CIUDAD, ubicacion.ciudad).apply()
        }
    }

    private fun transmisionCPGuardado() {
        // Si la llave es igual va a devolver el codigo postal actual
        val ciudad = preferencias.getString(KEY_CIUDAD, "")
        // Si hay data guardada la va a mandar a los observadores
        if(ciudad != null && ciudad.isNotBlank()) {
            _ubicacionGuardada.value = Ubicacion.Ciudad(ciudad)
        }
    }
}
