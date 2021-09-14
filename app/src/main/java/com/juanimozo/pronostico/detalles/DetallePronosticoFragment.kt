package com.juanimozo.pronostico.detalles

import com.juanimozo.pronostico.detalles.DetallePronosticoFragmentArgs
import com.juanimozo.pronostico.detalles.DetallePronosticoViewModel
import com.juanimozo.pronostico.detalles.DetallePronosticoViewModelFactory
import com.juanimozo.pronostico.detalles.DetallePronosticoViewState

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.juanimozo.pronostico.*
import com.juanimozo.pronostico.databinding.FragmentDetallePronosticoBinding

class DetallePronosticoFragment : Fragment() {

    // Traer los argumentos. "by" traee una clase delegada
    private val args: DetallePronosticoFragmentArgs by navArgs()
    // Dar acceso a la Factory
    private lateinit var viewModelFactory: DetallePronosticoViewModelFactory
    // viewModels delegado se encarga de funciones como, por ejemplo, manejar la data al cambiar la orientación del dispositivo
    private val viewModel: DetallePronosticoViewModel by viewModels(
            factoryProducer = { viewModelFactory }
    )

    private var _binding: FragmentDetallePronosticoBinding? = null
    // Esta propiedad solo es válida entre onCreateView y onDestroyView
    private val binding get()= _binding!!

    // Traer las shared preferences. Lateinit significa que la variable no tiene un valor ahora pero lo va a tener luego
    // Se utiliza porque cuando se usa necesita un contexto, el cual surge de onCreate
    private lateinit var managerUnidadTemperatura: ManagerUnidadTemperatura

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dar acceso al objeto binding
        _binding = FragmentDetallePronosticoBinding.inflate(inflater, container, false)
        viewModelFactory = DetallePronosticoViewModelFactory(args)
        managerUnidadTemperatura = ManagerUnidadTemperatura(requireContext())
        // root es autogenerado y devuelve toda la View, como un layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Crear el observer para ViewState
        val viewStateObserver = Observer<DetallePronosticoViewState> { viewState ->
            // Actualizar la UI
            // ToDo :: Para la 1.0 :: Agregar más información
            binding.tempText.text = formatoTemperatura(viewState.temperatura, managerUnidadTemperatura.getConfiguracionTemperatura())
            binding.descText.text = viewState.descripcion
            binding.fechaText.text = viewState.fecha
        }
        // Observar los cambios en ViewState
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Forzar la detención del binding para limpiar la memoria
        _binding = null
    }
}