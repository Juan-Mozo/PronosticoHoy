package com.juanimozo.pronostico.detalles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

private val FORMATO_FECHA = SimpleDateFormat("dd-MM-yyyy")

class DetallePronosticoViewModelFactory(private val args: DetallePronosticoFragmentArgs) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetallePronosticoViewModel::class.java)) {
            return DetallePronosticoViewModel(args) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}

class DetallePronosticoViewModel(args: DetallePronosticoFragmentArgs) : ViewModel() {
    // Recibir las instancias del ViewState
    private val _viewState: MutableLiveData<DetallePronosticoViewState> = MutableLiveData()
    // Enviar las nuevas instancias al Fragment
    val viewState: LiveData<DetallePronosticoViewState> = _viewState

    init {
        // Asignar el formato y enviar los valores como un ViewState al fragment
        _viewState.value = DetallePronosticoViewState(
                temperatura = args.temperatura,
                descripcion = args.descripcion,
                fecha = FORMATO_FECHA.format(Date(args.fecha * 1000))
        )
    }
}