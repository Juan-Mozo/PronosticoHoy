package com.juanimozo.pronostico

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.juanimozo.pronostico.api.ClimaActual
import com.juanimozo.pronostico.api.ClimaSemanal
import com.juanimozo.pronostico.api.PronosticoDiario
import com.juanimozo.pronostico.api.crearOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PronosticoRepositorio {

    private val _climaActual = MutableLiveData<ClimaActual>()
    val climaActual: LiveData<ClimaActual> = _climaActual

    // Actualiza la data y crea una lista de 7 días de pronóstico
    private val _climaSemanal = MutableLiveData<ClimaSemanal>()
    // Permite que la activity lea los cambios pero no puede modificar la data al ser Live data y no MutablLiveData
    val pronosticoSemanal: LiveData<ClimaSemanal> = _climaSemanal

    fun cargarPronosticoSemanal(codigoPostal: String) {
        // Llamar pronosticoActual para conseguir las coordenadas según el codigoPostal
        val call = crearOpenWeatherMapService().pronosticoActual(codigoPostal, "metric", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<ClimaActual> {
            override fun onResponse(
                    call: Call<ClimaActual>,
                    response: Response<ClimaActual>
            ) {
                val respuestaPronostico = response.body()
                if(respuestaPronostico != null) {
                    // Cargar el pronostico de 7 días
                    val pronosticoCall = crearOpenWeatherMapService().pronosticoSieteDias(
                        lat = respuestaPronostico.coord.lat,
                        lon = respuestaPronostico.coord.lon,
                        exclude = "current,minutely,hourly",
                        units = "metric",
                        apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                    )
                    pronosticoCall.enqueue(object: Callback<ClimaSemanal> {
                        override fun onResponse(call: Call<ClimaSemanal>, response: Response<ClimaSemanal>) {
                            val climaSemanalResponse = response.body()
                            if(climaSemanalResponse != null) {
                                // Actualizar la UI
                                _climaSemanal.value = climaSemanalResponse
                            }
                        }

                        override fun onFailure(call: Call<ClimaSemanal>, t: Throwable) {
                            Log.e(PronosticoRepositorio::class.java.simpleName, "Error 2", t)
                        }

                    })
                }
            }

            override fun onFailure(call: Call<ClimaActual>, t: Throwable) {
                Log.e(PronosticoRepositorio::class.java.simpleName, "Error 1", t)
            }

        })
    }

    fun cargarPronosticoActual(codigoPostal: String) {
        val call = crearOpenWeatherMapService().pronosticoActual(codigoPostal, "metric", BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object : Callback<ClimaActual> {
            override fun onResponse(
                    call: Call<ClimaActual>,
                    response: Response<ClimaActual>
            ) {
                val respuestaPronostico = response.body()
                if (respuestaPronostico != null) {
                    _climaActual.value = respuestaPronostico
                }
            }

            override fun onFailure(call: Call<ClimaActual>, t: Throwable) {
                Log.e(PronosticoRepositorio::class.java.simpleName, "Error al cargar Pronostico Actual")
            }
        })
    }
}