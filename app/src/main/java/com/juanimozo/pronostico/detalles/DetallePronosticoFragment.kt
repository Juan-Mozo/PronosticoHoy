package com.juanimozo.pronostico.detalles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.juanimozo.pronostico.*

class DetallePronosticoFragment : Fragment() {

    // Traer los argumentos
    private val args: DetallePronosticoFragmentArgs by navArgs()

    // Traer las shared preferences. Lateinit significa que la variable no tiene un valor ahora pero lo va a tener luego
    // Se utiliza porque cuando se usa necesita un contexto, el cual surge de onCreate
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_detalle_pronostico, container, false)

        managerUnidadTemperatura = ManagerUnidadTemperatura(requireContext())

        val detalleImagen = layout.findViewById<ImageView>(R.id.detalleImagen)
        val detalleDiaText = layout.findViewById<TextView>(R.id.detalleDiaText)
        val tempText = layout.findViewById<TextView>(R.id.tempText)
        val descripcionText = layout.findViewById<TextView>(R.id.descText)

        // recibir los datos desde main activity
        tempText.text = formatoTemperatura(args.temperatura, managerUnidadTemperatura.getConfiguracionTemperatura())
        // Pasar la data al texto
        descripcionText.text = args.descripcion

        return layout
    }
}