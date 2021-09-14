package com.juanimozo.pronostico.pronostico

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.juanimozo.pronostico.*
import com.juanimozo.pronostico.api.ClimaActual

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

        // Inflar el layout para este fragment
        val view = inflater.inflate(R.layout.fragment_pronostico_actual, container, false)
        val nombreUbicacion: TextView = view.findViewById(R.id.nombreUbicacion)
        val temperaturaText: TextView = view.findViewById(R.id.temperaturaText)
        val mensajeText: TextView = view.findViewById(R.id.mensajeText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        // Boton volver a home
        val ubicacionEntradaBoton : FloatingActionButton = view.findViewById(R.id.ubicacionEntradaBoton)
        ubicacionEntradaBoton.setOnClickListener {
            mostrarUbicacionEntrada()
        }

        // Observador que actualiza la UI
        val climaActualObserver = Observer<ClimaActual> { clima ->
            mensajeText.visibility = View.GONE
            progressBar.visibility = View.GONE
            temperaturaText.visibility = View.VISIBLE
            nombreUbicacion.visibility = View.VISIBLE
            nombreUbicacion.text = clima.name
            temperaturaText.text = formatoTemperatura(clima.main.temp, managerUnidadTemperatura.getConfiguracionTemperatura())
        }
        pronosticoRepositorio.climaActual.observe(viewLifecycleOwner, climaActualObserver)

        // Observador en Ubicacion (UbicacionRepositorio)
        ubicacionRepositorio = UbicacionRepositorio(requireContext())
        val ubicacionGuardadaObserver = Observer<Ubicacion> { ubicacionGuardada ->
            when(ubicacionGuardada) {
                is Ubicacion.CodigoPostal -> {
                    progressBar.visibility = View.VISIBLE
                    mensajeText.visibility = View.GONE
                    pronosticoRepositorio.cargarPronosticoActual(ubicacionGuardada.codigoPostal)
                }
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