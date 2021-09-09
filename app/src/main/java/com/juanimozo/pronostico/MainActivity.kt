package com.juanimozo.pronostico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.juanimozo.pronostico.pronostico.PronosticoActualFragment
import com.juanimozo.pronostico.pronostico.PronosticoActualFragmentDirections
import com.juanimozo.pronostico.ubicacion.UbicacionEntradaFragment
import com.juanimozo.pronostico.ubicacion.UbicacionEntradaFragmentDirections

class MainActivity : AppCompatActivity() {

    // Traer el manager para poder pasarle al adaptador la temperatura configurada
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        managerUnidadTemperatura = ManagerUnidadTemperatura(this)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setTitle(R.string.app_name)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)

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
}