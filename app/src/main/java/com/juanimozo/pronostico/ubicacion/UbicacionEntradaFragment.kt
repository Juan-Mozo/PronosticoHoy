package com.juanimozo.pronostico.ubicacion

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.juanimozo.pronostico.R
import com.juanimozo.pronostico.Ubicacion
import com.juanimozo.pronostico.UbicacionRepositorio
import com.juanimozo.pronostico.pronostico.PronosticoSemanalFragmentDirections

class UbicacionEntradaFragment : Fragment() {

    private lateinit var ubicacionRepositorio: UbicacionRepositorio

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ubicacionRepositorio = UbicacionRepositorio(requireContext())

        val view = inflater.inflate(R.layout.fragment_ubicacion_entrada, container, false)

        val ciudadEditText: EditText = view.findViewById(R.id.ciudadEditText)
        val enviarButton: Button = view.findViewById(R.id.enviarButton)

        enviarButton.setOnClickListener {
            val ciudad: String = ciudadEditText.text.toString()
            // Chequear si el código postal tiene los caracteres necesarios
            if (ciudad == null) {
                Toast.makeText(requireContext(), R.string.mensajeErrorCP, Toast.LENGTH_SHORT).show()
            } else {
                ubicacionRepositorio.guardarUbicacion(Ubicacion.Ciudad(ciudad))
                val accion = UbicacionEntradaFragmentDirections.actionUbicacionEntradaFragmentToPronosticoActualFragment()
                findNavController().navigate(accion)
            }
        }

        return view
    }
}