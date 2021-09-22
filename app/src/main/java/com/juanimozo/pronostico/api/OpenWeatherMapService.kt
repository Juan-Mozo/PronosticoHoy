package com.juanimozo.pronostico.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ToDo :: Para version 1.1 :: Cambiar tipo de @Get en pronosticoActual
fun crearOpenWeatherMapService(): OpenWeatherMapService {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    // Crear una implementacion del API
    return retrofit.create(OpenWeatherMapService::class.java)
}

// Modelos para las Responses
interface OpenWeatherMapService {
    @GET("/data/2.5/weather")
    fun pronosticoActual(
        // @Query("zip") codigoPostal: String,
            @Query("q") ciudad: String,
            @Query("units") unidad: String,
            @Query("appid") apiKey: String,
            @Query("lang") lang: String
    ): Call<ClimaActual>

    @GET("/data/2.5/onecall")
    fun pronosticoSieteDias(
            @Query("lat") lat: Float,
            @Query("lon") lon: Float,
            @Query("exclude") exclude: String,
            @Query("units") units: String,
            @Query("lang") lang: String,
            @Query("appid") apiKey: String
    ): Call<ClimaSemanal>
}
