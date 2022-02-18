package com.applaunch.api

import retrofit2.Response
import retrofit2.http.GET

interface WeatherApi {
    @GET("data/2.5/onecall?lat=12.9082847623315&lon=77.65197822993314&units=imperial&appid=b143bb707b2ee117e62649b358207d3e")
    suspend fun getWeatherDetails(): Response<Weather>
}