package com.compunetlimited.ogan.myweatherapp

/**
 * Created by belema on 10/20/17.
 */

//data class which gives me get, set, toString and hashcode
//so I don't have to code them myself
//I access the fields like properties in C#
data class Weather (
        val id : Int,
        val main: String,
        val icon: String
)

data class Main(
        val temp : Double
)

data class Sys(
        val sunrise: Long,
        val sunset: Long
)

//this is the class of the response I get from the api call
data class WeatherResult(
        val weather: List<Weather>,
        val main : Main,
        val sys: Sys,
        val name: String
)