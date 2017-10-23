package com.compunetlimited.ogan.myweatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var weatherIcon: ImageView
    private lateinit var temp: TextView
    private lateinit var temperatureDescription: TextView
    private lateinit var sunsetTextView: TextView
    private lateinit var sunriseTextView: TextView
    private lateinit var call: Call<WeatherResult>
    private lateinit var containerView: LinearLayout
    private lateinit var loader: ProgressBar
    private lateinit var searchInput: EditText
    private lateinit var city: String
    private lateinit var errorTextView: TextView
    private lateinit var cityTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_layout)

        weatherIcon = findViewById(R.id.weather_icon)
        temp = findViewById(R.id.temp_tv)
        temperatureDescription = findViewById(R.id.temp_desc)
        sunsetTextView = findViewById(R.id.sunset_tv)
        sunriseTextView = findViewById(R.id.sunrise_tv)
        containerView = findViewById(R.id.view_group)
        loader = findViewById(R.id.progress_bar)
        errorTextView = findViewById(R.id.error_textview)
        cityTextView = findViewById(R.id.city_tv)

        searchInput = findViewById(R.id.city)
        val button: Button = findViewById(R.id.search_btn)
        button.setOnClickListener{
            startLoading()
            loadData()
        }

    }

    private fun showLoadDone(){
        searchInput.text.clear()
        loader.visibility = View.GONE
        containerView.visibility = View.VISIBLE
    }

    private fun startLoading(){
        loader.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        containerView.visibility = View.GONE
    }


    //method for making the api request
    private fun loadData()  {
        val weatherService = ServiceGenerator.createService(WeatherService::class.java)

        val appid: String = resources.getString(R.string.api_id)
        val units: String = resources.getString(R.string.api_unit)

        city = searchInput.text.toString()
        call = weatherService.getWeather(city, appid, units)

        call.enqueue(object: Callback<WeatherResult> {
            override fun onFailure(call: Call<WeatherResult>?, t: Throwable?) {
                showLoadDone()
                errorTextView.text = resources.getString(R.string.error_text)
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
                            sunsetTextView.text = "Sunset: ${whatIsTheTimeFor(sunset)}"

                            sunriseTextView.text = "Sunrise: ${whatIsTheTimeFor(sunrise)}"

                            temp.text = "Temperature: ${result.main.temp}°C"

                            temperatureDescription.text = "Description: ${result.weather[0].main}"

                            cityTextView.text = "City: ${result.name}"

                            //loading the image into the view using picasso
                            Picasso.with(this@MainActivity)
                                    .load("http://openweathermap.org/img/w/${result.weather[0].icon}.png")
                                    .into(weatherIcon)
                        }

                    } else{
                        errorTextView.text = response.message()
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
