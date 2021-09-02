package com.juanimozo.pronostico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juanimozo.pronostico.detalles.DetallePronosticoActivity

class MainActivity : AppCompatActivity() {

    private val pronosticoRepositorio = PronosticoRepositorio()
    // Traer el manager para poder pasarle al adaptador la temperatura configurada
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    // #ToDo: 1 Buscar nombre autor iconos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        managerUnidadTemperatura = ManagerUnidadTemperatura(this)

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
        val pronosticoDiarioAdaptador = PronosticoDiarioAdaptador(managerUnidadTemperatura) {  pronostico ->
            mostrarDetallesPronostico(pronostico)
        }
        listaPronostico.adapter = pronosticoDiarioAdaptador

        // Observador
        val pronosticoSemanalObserver = Observer<List<PronosticoDiario>> { itemsPronostico ->
            // actualiza el adaptador de la lista
            pronosticoDiarioAdaptador.submitList(itemsPronostico)
        }

        pronosticoRepositorio.pronosticoSemanal.observe(this, pronosticoSemanalObserver)

    }

    // Inflar el menu de configuraciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.setting_menu, menu)
        return true
    }
    // Manejar cuando se selecciona un item del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.configuracionUnidades -> {
                mostrarUnidadesDialogo()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    // Mostrar el dialogo para seleccionar el tipo de unidades
    private fun mostrarUnidadesDialogo() {
        val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("Seleccionar el tipo de unidad")
                .setMessage("Seleccionar que unidad de temperatura utilizar")
                .setNegativeButton("C°") {_, _ ->
                    managerUnidadTemperatura.updatePreferencias(UnidadTemperatura.Celsius)
                }
                .setPositiveButton("F°") {_, _ ->
                    managerUnidadTemperatura.updatePreferencias(UnidadTemperatura.Fahreinheit)
                }
                .setOnDismissListener {
                    Toast.makeText(this, "Los cambios se aplicarán al reiniciar la app", Toast.LENGTH_SHORT).show()
                }
                .show()
    }

    // Llamar actividad detalles y pasar la data
    private fun mostrarDetallesPronostico(pronostico: PronosticoDiario) {
        val detallePronosticoIntent = Intent(this, DetallePronosticoActivity::class.java)
        detallePronosticoIntent.putExtra("key_temp", pronostico.temperatura)
        detallePronosticoIntent.putExtra("key_descripcion", pronostico.descripcion)
        startActivity(detallePronosticoIntent)
    }
}