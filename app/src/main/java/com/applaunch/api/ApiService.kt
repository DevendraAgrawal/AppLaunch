package com.applaunch.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

    val baseUrl = "https://api.openweathermap.org/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object AppLaunchApi {
    val appLaunchService: WeatherApi by lazy {
        ApiService().getInstance().create(WeatherApi::class.java)
    }
}