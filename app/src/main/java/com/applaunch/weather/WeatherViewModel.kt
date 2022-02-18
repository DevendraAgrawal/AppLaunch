package com.applaunch.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applaunch.api.AppLaunchApi
import com.applaunch.api.Weather
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherViewModel: ViewModel() {

    private var _weatherDetails = MutableLiveData<Weather>()
    val weatherDetails: LiveData<Weather>
        get() = _weatherDetails

    fun getWeatherDetails(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    AppLaunchApi.appLaunchService.getWeatherDetails().apply {
                        Log.e("Data", Gson().toJson(this.body()))
                        _weatherDetails.postValue(this.body())
                    }
                }catch (e: retrofit2.HttpException){
                    when(e.code()){
                        401 -> {

                        }
                    }
                }catch (ex: Exception){
                    ex.printStackTrace()
                }
            }
        }
    }
}