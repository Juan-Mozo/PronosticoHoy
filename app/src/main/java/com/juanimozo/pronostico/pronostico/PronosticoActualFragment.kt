package com.juanimozo.pronostico.pronostico

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.juanimozo.pronostico.*
import com.juanimozo.pronostico.detalles.DetallePronosticoActivity

class PronosticoActualFragment : Fragment() {
    private val pronosticoRepositorio = PronosticoRepositorio()
    // Traer el manager para poder pasarle al adaptador la temperatura configurada
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    private lateinit var appNavegador: AppNavegador

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavegador = context as AppNavegador
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        managerUnidadTemperatura = ManagerUnidadTemperatura(requireContext())

        val codigoPostal = requireArguments().getString(KEY_CODIGOPOSTAL) ?: ""

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pronostico_actual, container, false)

        // Boton volver a home
        val ubicacionEntradaBoton : FloatingActionButton = view.findViewById(R.id.ubicacionEntradaBoton)
        ubicacionEntradaBoton.setOnClickListener {
            appNavegador.navegarAUbicacionEntrada()
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
        val pronosticoSemanalObserver = Observer<List<PronosticoDiario>> { itemsPronostico ->
            // actualiza el adaptador de la lista
            pronosticoDiarioAdaptador.submitList(itemsPronostico)
        }
        pronosticoRepositorio.pronosticoSemanal.observe(viewLifecycleOwner, pronosticoSemanalObserver)

        pronosticoRepositorio.cargarPronostico(codigoPostal)

        return view
    }

    // Llamar actividad detalles y pasar la data
    private fun mostrarDetallesPronostico(pronostico: PronosticoDiario) {
        val detallePronosticoIntent = Intent(requireContext(), DetallePronosticoActivity::class.java)
        detallePronosticoIntent.putExtra("key_temp", pronostico.temperatura)
        detallePronosticoIntent.putExtra("key_descripcion", pronostico.descripcion)
        startActivity(detallePronosticoIntent)
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