package com.compunetlimited.ogan.myweatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var call: Call<WeatherResult>
    private lateinit var city: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_layout)

        search_btn.setOnClickListener{
            startLoading()
            loadData()
        }

    }

    private fun showLoadDone(){
        search_input.text.clear()
        progress_bar.visibility = View.GONE
        view_group.visibility = View.VISIBLE
    }

    private fun startLoading(){
        progress_bar.visibility = View.VISIBLE
        error_textview.visibility = View.GONE
        view_group.visibility = View.GONE
    }


    //method for making the api request
    private fun loadData()  {
        val weatherService = ServiceGenerator.createService(WeatherService::class.java)

        val appid = resources.getString(R.string.api_id)
        val units = resources.getString(R.string.api_unit)

        city = search_input.text.toString()
        call = weatherService.getWeather(city, appid, units)

        call.enqueue(object: Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>?, t: Throwable?) {
                showLoadDone()
                error_textview.text = resources.getString(R.string.error_text)
            }

            override fun onResponse(call: Call<WeatherResult>?, response: Response<WeatherResult>?) {
                showLoadDone()
                if (response != null) {
                    if (response.isSuccessful){

                        val result = response.body()

                        if (result != null){
                            //collecting the date time data in unix time stamp and converting it
                            val sunset = result.sys.sunset
                            val sunrise = result.sys.sunrise

                            //using string interpolation and whatIsTimeFor method
                            // to convert unix time to 24 hr time in GMT + 1
                            sunset_tv.text = "Sunset: ${whatIsTheTimeFor(sunset)}"

                            sunrise_tv.text = "Sunrise: ${whatIsTheTimeFor(sunrise)}"

                            temp_tv.text = "Temperature: ${result.main.temp}°C"

                            temp_desc.text = "Description: ${result.weather[0].main}"

                            city_tv.text = "City: ${result.name}"

                            //loading the image into the view using picasso
                            Picasso.with(this@MainActivity)
                                    .load("http://openweathermap.org/img/w/${result.weather[0].icon}.png")
                                    .into(weather_icon)
                        }

                    } else{
                        error_textview.text = response.message()
                    }
                }

            }
        })
    }

    //created a method so I don't have to
    //repeat myself checking time for sunset and sunrise
    fun whatIsTheTimeFor(time: Long): String{
        val date = Date(time * 1000L)
        val sdf = SimpleDateFormat("HH:mm:ss z", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT+1")
        return sdf.format(date)
    }
}
