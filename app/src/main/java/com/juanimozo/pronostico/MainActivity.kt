package com.juanimozo.pronostico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.juanimozo.pronostico.pronostico.PronosticoActualFragment
import com.juanimozo.pronostico.ubicacion.UbicacionEntradaFragment

class MainActivity : AppCompatActivity(), AppNavegador {

    // Traer el manager para poder pasarle al adaptador la temperatura configurada
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    // #ToDo: 1 Buscar nombre autor iconos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        managerUnidadTemperatura = ManagerUnidadTemperatura(this)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, UbicacionEntradaFragment())
                .commit()

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
                mostrarUnidadesDialogo(this, managerUnidadTemperatura)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun navegarAPronosticoActual(codigoPostal: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, PronosticoActualFragment.nuevaInstancia(codigoPostal))
            .commit()
    }

    override fun navegarAUbicacionEntrada() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, UbicacionEntradaFragment())
                .commit()
    }
}