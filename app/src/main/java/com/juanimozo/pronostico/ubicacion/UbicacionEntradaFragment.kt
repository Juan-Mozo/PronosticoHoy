package com.juanimozo.pronostico.ubicacion

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.juanimozo.pronostico.AppNavegador
import com.juanimozo.pronostico.R

class UbicacionEntradaFragment : Fragment() {

    private lateinit var appNavegador: AppNavegador

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavegador = context as AppNavegador
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ubicacion_entrada, container, false)

        val codigoPostalEditText: EditText = view.findViewById(R.id.codigoPostalEditText)
        val enviarButton: Button = view.findViewById(R.id.enviarButton)

        enviarButton.setOnClickListener {
            val codigoPostal: String = codigoPostalEditText.text.toString()
            // Chequear si el código postal tiene los caracteres necesarios
            if (codigoPostal.length != 4) {
                Toast.makeText(requireContext(), R.string.mensajeErrorCP, Toast.LENGTH_SHORT).show()
            } else {
                appNavegador.navegarAPronosticoActual(codigoPostal)
            }
        }

        return view
    }
}