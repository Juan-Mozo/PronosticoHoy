package com.juanimozo.pronostico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val pronosticoRepositorio = PronosticoRepositorio()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val codigoPostalEditText: EditText = findViewById(R.id.codigoPostalEditText)
        val enviarButton: Button = findViewById(R.id.enviarButton)

        enviarButton.setOnClickListener {
            val codigoPostal: String = codigoPostalEditText.text.toString()
            // Chequear si el código postal tiene los caracteres necesarios
            if (codigoPostal.length != 4) {
                Toast.makeText(this, R.string.mensajeErrorCP, Toast.LENGTH_SHORT).show()
            } else {
                pronosticoRepositorio.cargarPronostico(codigoPostal)
            }
        }

        // Observador
        val pronosticoSemanalObserver = Observer<List<PronosticoDiario>> { itemsPronostico ->
            // actualiza el adaptador de la lista
            Toast.makeText(this, "Items Cargador", Toast.LENGTH_SHORT).show()
        }

        pronosticoRepositorio.pronosticoSemanal.observe(this, pronosticoSemanalObserver)

    }
}