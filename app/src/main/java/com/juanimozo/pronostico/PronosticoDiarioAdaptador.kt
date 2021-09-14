package com.juanimozo.pronostico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juanimozo.pronostico.api.PronosticoDiario
import java.text.SimpleDateFormat
import java.util.*

// Formato fecha
private val DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy")

// ViewHolder
class PronosticoDiarioViewHolder(
        view: View,
        private val managerUnidadTemperatura: ManagerUnidadTemperatura
        )
    : RecyclerView.ViewHolder(view) {
    // view antes de findViewById porque el adaptador no tiene el contexto al no ser una activity
    private val temperaturaText = view.findViewById<TextView>(R.id.temperaturaText)
    private val descripcionText = view.findViewById<TextView>(R.id.descripcionText)
    private val dateText = view.findViewById<TextView>(R.id.dateText)
    private val pronosticoIcon = view.findViewById<ImageView>(R.id.pronosticoIcon)

    // Conecta individualmente cada item con las view de arriba
    fun bind(pronosticoDiario: PronosticoDiario) {
        temperaturaText.text = formatoTemperatura(pronosticoDiario.temp.max, managerUnidadTemperatura.getConfiguracionTemperatura())
        descripcionText.text = pronosticoDiario.weather[0].description
        dateText.text = DATE_FORMAT.format(Date(pronosticoDiario.dt * 1000))
        cambiarIcon(pronosticoDiario)
    }

    // Actualizar el icono según la descripción dada por la api
    fun cambiarIcon(pronosticoDiario: PronosticoDiario) {
        when(pronosticoDiario.weather[0].description) {
            "lluvia ligera" -> pronosticoIcon.setImageResource(R.drawable.ic_lluvia_ligera)
            "lluvia moderada" -> pronosticoIcon.setImageResource(R.drawable.ic_luvia)
            "heavy intensity rain" -> pronosticoIcon.setImageResource(R.drawable.ic_luvia)
            "cielo claro" -> pronosticoIcon.setImageResource(R.drawable.ic_soleado)
            "nubes dispersas" -> pronosticoIcon.setImageResource(R.drawable.ic_ligeramente_nublado)
            "algo de nubes" -> pronosticoIcon.setImageResource(R.drawable.ic_ligeramente_nublado)
            "nubes" -> pronosticoIcon.setImageResource(R.drawable.ic_parc_nublado)
            "muy nuboso" -> pronosticoIcon.setImageResource(R.drawable.ic_nublado)
            else -> pronosticoIcon.setImageResource(R.drawable.ic_soleado)
        }
    }
}

class PronosticoDiarioAdaptador(
        private val managerUnidadTemperatura: ManagerUnidadTemperatura,
        private val clickHandler: (PronosticoDiario) -> Unit
) : ListAdapter<PronosticoDiario, PronosticoDiarioViewHolder>(DIFF_CONFIG) {

    // Movida rara para completar el adaptador ??
    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<PronosticoDiario>() {
            //Devuelve solo si oldItem y newItem son lo mismo
            override fun areItemsTheSame(oldItem: PronosticoDiario, newItem: PronosticoDiario): Boolean {
                return oldItem === newItem
            }

            //Devuelve solo si el contenido entre oldItem y newItem son lo mismo
            override fun areContentsTheSame(oldItem: PronosticoDiario, newItem: PronosticoDiario): Boolean {
                return oldItem == newItem
            }

        }
    }

    // Crea el nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PronosticoDiarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pronostico_diario, parent, false)
        return PronosticoDiarioViewHolder(itemView, managerUnidadTemperatura)
    }

    // Agarra individualmente cada item y lo pasa como grupo al ViewHolder
    override fun onBindViewHolder(holder: PronosticoDiarioViewHolder, position: Int) {
        // Accede a los items y los posiciona
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}