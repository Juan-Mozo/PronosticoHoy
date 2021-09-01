package com.juanimozo.pronostico.detalles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.juanimozo.pronostico.R
import com.juanimozo.pronostico.formatoTemperatura

class DetallePronosticoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pronostico)

        setTitle(R.string.DetallePronostico)

        val detalleImagen = findViewById<ImageView>(R.id.detalleImagen)
        val detalleDiaText = findViewById<TextView>(R.id.detalleDiaText)
        val tempText = findViewById<TextView>(R.id.tempText)
        val descripcionText = findViewById<TextView>(R.id.descText)

        // recibir los datos desde main activity
        val temperatura = intent.getFloatExtra("key_temp", 0f)
        val descripcion = intent.getStringExtra("key_descripcion")

        tempText.text = formatoTemperatura(temperatura)
        descripcionText.text = descripcion
    }
}