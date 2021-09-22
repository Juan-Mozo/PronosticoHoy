package com.juanimozo.pronostico.pronostico

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.juanimozo.pronostico.*
import com.juanimozo.pronostico.api.ClimaSemanal
import com.juanimozo.pronostico.api.PronosticoDiario

class PronosticoSemanalFragment : Fragment() {
    private val pronosticoRepositorio = PronosticoRepositorio()

    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura
    private lateinit var ubicacionRepositorio: UbicacionRepositorio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        managerUnidadTemperatura = ManagerUnidadTemperatura(requireContext())

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pronostico_semanal, container, false)
        val mensajeText: TextView = view.findViewById(R.id.mensajeText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        // Boton volver a home
        val ubicacionEntradaBoton : FloatingActionButton = view.findViewById(R.id.ubicacionEntradaBoton)
        ubicacionEntradaBoton.setOnClickListener {
            mostrarUbicacionEntrada()
        }

        // RecyclerView semanal
        val listaPronostico: RecyclerView = view.findViewById(R.id.listaPronostico)
        listaPronostico.layoutManager = LinearLayoutManager(requireContext())
        // Adaptador al RecyclerView
        val pronosticoDiarioAdaptador = PronosticoDiarioAdaptador(managerUnidadTemperatura) {  pronostico ->
            mostrarDetallesPronostico(pronostico)
        }
        listaPronostico.adapter = pronosticoDiarioAdaptador

        // Observador que actualiza la UI
        val pronosticoSemanalObserver = Observer<ClimaSemanal> { climaSemanal ->
            listaPronostico.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            // actualiza el adaptador de la lista
            pronosticoDiarioAdaptador.submitList(climaSemanal.daily)
        }
        pronosticoRepositorio.pronosticoSemanal.observe(viewLifecycleOwner, pronosticoSemanalObserver)

        ubicacionRepositorio = UbicacionRepositorio(requireContext())
        val ubicacionGuardadaObserver = Observer<Ubicacion> { ubicacionGuardada ->
            when (ubicacionGuardada) {
                is Ubicacion.Ciudad -> {
                    progressBar.visibility = View.VISIBLE
                    mensajeText.visibility = View.GONE
                    pronosticoRepositorio.cargarPronosticoSemanal(ubicacionGuardada.ciudad)
                }
            }
        }
            ubicacionRepositorio.ubicacionGuardada.observe(viewLifecycleOwner, ubicacionGuardadaObserver)

        return view
    }

    private fun mostrarUbicacionEntrada() {
        val accion = PronosticoSemanalFragmentDirections.actionPronosticoSemanalFragmentToUbicacionEntradaFragment()
        findNavController().navigate(accion)
    }

    // Llamar actividad detalles y pasar la data
    private fun mostrarDetallesPronostico(pronostico: PronosticoDiario) {
        val temperatura = pronostico.temp.max
        val descripcion = pronostico.weather[0].description
        val fecha = pronostico.dt
        val accion = PronosticoSemanalFragmentDirections.actionPronosticoSemanalFragmentToDetallePronosticoFragment(temperatura, descripcion, fecha)
        findNavController().navigate(accion)
    }

    companion object {
        // Crear una llave para pasar el codigo postal
        const val KEY_CODIGOPOSTAL = "key_codigopostal"

        // Pasarle el codigo postal a onCreate
        fun nuevaInstancia(codigoPostal: String) : PronosticoSemanalFragment {
            val fragment = PronosticoSemanalFragment()

            val args = Bundle()
            args.putString(KEY_CODIGOPOSTAL, codigoPostal)
            fragment.arguments = args

            return fragment
        }
    }

}