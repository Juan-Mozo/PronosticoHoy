package com.juanimozo.pronostico.pronostico

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val codigoPostal = arguments?.getString(KEY_CODIGOPOSTAL)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pronostico_semanal, container, false)

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
            // actualiza el adaptador de la lista
            pronosticoDiarioAdaptador.submitList(climaSemanal.daily)
        }
        pronosticoRepositorio.pronosticoSemanal.observe(viewLifecycleOwner, pronosticoSemanalObserver)

        ubicacionRepositorio = UbicacionRepositorio(requireContext())
        val ubicacionGuardadaObserver = Observer<Ubicacion> { ubicacionGuardada ->
            when (ubicacionGuardada) {
                is Ubicacion.CodigoPostal -> pronosticoRepositorio.cargarPronosticoSemanal(ubicacionGuardada.codigoPostal)
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
        val accion = PronosticoSemanalFragmentDirections.actionPronosticoSemanalFragmentToDetallePronosticoFragment(temperatura, descripcion)
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