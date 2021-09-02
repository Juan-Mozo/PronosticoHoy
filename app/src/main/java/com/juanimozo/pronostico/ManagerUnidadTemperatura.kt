package com.juanimozo.pronostico

import android.content.Context

enum class UnidadTemperatura {
    Fahreinheit, Celsius
}

class ManagerUnidadTemperatura(context: Context) {
    // Asignar el Shared preferences object
    private val preferencias = context.getSharedPreferences("configuracion", Context.MODE_PRIVATE)

    // Editar preferencias según el string de UnidadTemperatura
    fun updatePreferencias(configuracion: UnidadTemperatura) {
        preferencias.edit().putString("key_temp_display", configuracion.name).commit()
    }

    // Recuperar la configuración
    fun getConfiguracionTemperatura() : UnidadTemperatura {
        // Si lo que está a la izquierda de ?: devuelve null entonces se pasa como valor lo de la derecha
        val establecerValor = preferencias.getString("key_temp_display", UnidadTemperatura.Celsius.name) ?: UnidadTemperatura.Celsius.name
        return UnidadTemperatura.valueOf(establecerValor)
    }
}