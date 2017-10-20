package com.compunetlimited.ogan.myweatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by belema on 10/20/17.
 */
interface WeatherService {
    @GET("weather")
    fun getWeather(@Query("q") city: String, @Query("appid") appid: String, @Query("units") metrics: String) : Call<WeatherResult>
}