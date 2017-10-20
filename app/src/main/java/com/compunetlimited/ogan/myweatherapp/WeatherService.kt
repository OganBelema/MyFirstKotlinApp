package com.compunetlimited.ogan.myweatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by belema on 10/20/17.
 * Method for getting the current weather of a city
 * with the api
 * @param q is for the city and coutry
 * @param units is the detemine the format of the temperature
 * @param appid is my api key
 * whether celsius or kelvin or fahrenheit
 */
interface WeatherService {
    @GET("weather")
    fun getWeather(@Query("q") city: String, @Query("appid") appid: String, @Query("units") metrics: String) : Call<WeatherResult>
}