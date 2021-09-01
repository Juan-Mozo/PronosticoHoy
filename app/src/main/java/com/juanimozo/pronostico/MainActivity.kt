package com.juanimozo.pronostico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        // RecyclerView semanal
        val listaPronostico: RecyclerView = findViewById(R.id.listaPronostico)
        listaPronostico.layoutManager = LinearLayoutManager(this)
            // Adaptador al RecyclerView
        val pronosticoDiarioAdaptador = PronosticoDiarioAdaptador() {  itemPronostico ->
            val msg = getString(R.string.itemPronostico_seleccionado_formato, itemPronostico.temperatura, itemPronostico.descripcion)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        listaPronostico.adapter = pronosticoDiarioAdaptador

        // Observador
        val pronosticoSemanalObserver = Observer<List<PronosticoDiario>> { itemsPronostico ->
            // actualiza el adaptador de la lista
            pronosticoDiarioAdaptador.submitList(itemsPronostico)
        }

        pronosticoRepositorio.pronosticoSemanal.observe(this, pronosticoSemanalObserver)

    }
}