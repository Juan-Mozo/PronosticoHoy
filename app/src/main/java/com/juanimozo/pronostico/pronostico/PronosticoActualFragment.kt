package com.juanimozo.pronostico.pronostico

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.juanimozo.pronostico.*
import com.juanimozo.pronostico.api.ClimaActual
import com.juanimozo.pronostico.api.PronosticoDiario
import com.juanimozo.pronostico.detalles.DetallePronosticoFragment

class PronosticoActualFragment : Fragment() {

    private val pronosticoRepositorio = PronosticoRepositorio()
    // Traer el manager para poder pasarle al adaptador la temperatura configurada
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura
    private lateinit var ubicacionRepositorio: UbicacionRepositorio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        managerUnidadTemperatura = ManagerUnidadTemperatura(requireContext())

        val codigoPostal = arguments?.getString(KEY_CODIGOPOSTAL)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pronostico_actual, container, false)
        val nombreUbicacion: TextView = view.findViewById(R.id.nombreUbicacion)
        val temperaturaText: TextView = view.findViewById(R.id.temperaturaText)

        // Boton volver a home
        val ubicacionEntradaBoton : FloatingActionButton = view.findViewById(R.id.ubicacionEntradaBoton)
        ubicacionEntradaBoton.setOnClickListener {
            mostrarUbicacionEntrada()
        }

        // Observador que actualiza la UI
        val climaActualObserver = Observer<ClimaActual> { clima ->
            nombreUbicacion.text = clima.name
            temperaturaText.text = formatoTemperatura(clima.main.temp, managerUnidadTemperatura.getConfiguracionTemperatura())
        }
        pronosticoRepositorio.climaActual.observe(viewLifecycleOwner, climaActualObserver)

        // Observador en Ubicacion (UbicacionRepositorio)
        ubicacionRepositorio = UbicacionRepositorio(requireContext())
        val ubicacionGuardadaObserver = Observer<Ubicacion> { ubicacionGuardada ->
            when(ubicacionGuardada) {
                is Ubicacion.CodigoPostal -> pronosticoRepositorio.cargarPronosticoActual(ubicacionGuardada.codigoPostal)
            }
        }
        ubicacionRepositorio.ubicacionGuardada.observe(viewLifecycleOwner, ubicacionGuardadaObserver)

        return view
    }

    // Volver al fragment UbicacionEntrada
    private fun mostrarUbicacionEntrada() {
        val accion = PronosticoActualFragmentDirections.actionPronosticoActualFragmentToUbicacionEntradaFragment()
        findNavController().navigate(accion)
    }

    companion object {
        // Crear una llave para pasar el codigo postal
        const val KEY_CODIGOPOSTAL = "key_codigopostal"

        // Pasarle el codigo postal a onCreate
        fun nuevaInstancia(codigoPostal: String) : PronosticoActualFragment {
            val fragment = PronosticoActualFragment()

            val args = Bundle()
            args.putString(KEY_CODIGOPOSTAL, codigoPostal)
            fragment.arguments = args

            return fragment
        }
    }

}