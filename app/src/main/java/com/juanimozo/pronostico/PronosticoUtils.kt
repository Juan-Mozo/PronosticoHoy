package com.juanimozo.pronostico

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatoTemperatura(temp: Float, unidadTemperatura: UnidadTemperatura): String {
    return when(unidadTemperatura) {
        UnidadTemperatura.Fahreinheit -> {
            val temp = (temp + 32f)
            String.format("%.1f°", temp)
        }
        UnidadTemperatura.Celsius -> String.format("%.1f°", temp)
    }
}

// Mostrar el dialogo para seleccionar el tipo de unidades
fun mostrarUnidadesDialogo(context: Context, managerUnidadTemperatura: ManagerUnidadTemperatura) {
    val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Seleccionar el tipo de unidad")
            .setMessage("Seleccionar que unidad de temperatura utilizar")
            .setNegativeButton("C°") {_, _ ->
                managerUnidadTemperatura.updatePreferencias(UnidadTemperatura.Celsius)
            }
            .setPositiveButton("F°") {_, _ ->
                managerUnidadTemperatura.updatePreferencias(UnidadTemperatura.Fahreinheit)
            }
            .setOnDismissListener {
                Toast.makeText(context, "Los cambios se aplicarán al reiniciar la app", Toast.LENGTH_SHORT).show()
            }
            .show()
}