package com.applaunch.api

data class Weather(
    val lat: String,
    val lon: String,
    val current: Current
)

data class Current(val temp: String, val humidity: String, val wind_speed: String, val weather: List<WeatherDetails>)

data class WeatherDetails(val id: String, val main: String, val description: String)