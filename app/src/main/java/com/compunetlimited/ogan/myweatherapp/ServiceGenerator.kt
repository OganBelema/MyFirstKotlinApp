package com.compunetlimited.ogan.myweatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by belema on 9/11/17
 */

object ServiceGenerator {

    private val BASE_URL = "http://api.openweathermap.org/data/2.5/"


    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()


    fun <S> createService(
            serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}

