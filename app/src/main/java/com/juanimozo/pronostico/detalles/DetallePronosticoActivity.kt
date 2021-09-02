package com.juanimozo.pronostico.detalles

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.juanimozo.pronostico.ManagerUnidadTemperatura
import com.juanimozo.pronostico.R
import com.juanimozo.pronostico.UnidadTemperatura
import com.juanimozo.pronostico.formatoTemperatura

class DetallePronosticoActivity : AppCompatActivity() {

    // Traer las shared preferences. Lateinit significa que la variable no tiene un valor ahora pero lo va a tener luego
    // Se utiliza porque cuando se usa necesita un contexto, el cual surge de onCreate
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pronostico)

        // Pasar el contexto a la variable lateinit
        managerUnidadTemperatura = ManagerUnidadTemperatura(this)

        setTitle(R.string.DetallePronostico)

        val detalleImagen = findViewById<ImageView>(R.id.detalleImagen)
        val detalleDiaText = findViewById<TextView>(R.id.detalleDiaText)
        val tempText = findViewById<TextView>(R.id.tempText)
        val descripcionText = findViewById<TextView>(R.id.descText)

        // recibir los datos desde main activity
        val temperatura = intent.getFloatExtra("key_temp", 0f)
        val descripcion = intent.getStringExtra("key_descripcion")
        // Pasar la data al texto
        tempText.text = formatoTemperatura(temperatura, managerUnidadTemperatura.getConfiguracionTemperatura())
        descripcionText.text = descripcion
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

}