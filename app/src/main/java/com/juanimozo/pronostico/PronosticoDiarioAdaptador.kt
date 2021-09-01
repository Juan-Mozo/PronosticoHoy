package com.juanimozo.pronostico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// ViewHolder
class PronosticoDiarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // view antes de findViewById porque el adaptador no tiene el contexto al no ser una activity
    private val temperaturaText = view.findViewById<TextView>(R.id.temperaturaText)
    private val descripcionText = view.findViewById<TextView>(R.id.descripcionText)

    // Conecta individualmente cada item con las view de arriba
    fun bind(pronosticoDiario: PronosticoDiario) {
        temperaturaText.text = formatoTemperatura(pronosticoDiario.temperatura)
        descripcionText.text = pronosticoDiario.descripcion
    }
}

class PronosticoDiarioAdaptador(
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
        return PronosticoDiarioViewHolder(itemView)
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