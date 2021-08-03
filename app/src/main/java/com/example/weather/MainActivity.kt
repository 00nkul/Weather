package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val textView :TextView = findViewById(R.id.temp)
        val tvToday :TextView = findViewById(R.id.tvToday)
        val tvToday1 :TextView = findViewById(R.id.tvToday1)
        val tvTom:TextView = findViewById(R.id.tvTom)
        val tvTom1:TextView = findViewById(R.id.tvTom1)
        val tvNext :TextView = findViewById(R.id.tvNext)
        val tvNext1 :TextView = findViewById(R.id.tvNext1)
        val tvAqi :TextView = findViewById(R.id.aqi)
        val tvWeather :TextView = findViewById(R.id.tvWeather)
        val tvSunrise :TextView = findViewById(R.id.sunRise)
        val tvSunSet :TextView = findViewById(R.id.sunSet)
        val tvHumidity :TextView = findViewById(R.id.humidity)
        val tvPressure :TextView = findViewById(R.id.pressure)
        val tvUvIndex :TextView = findViewById(R.id.uvIndex)
        val tvWindSpeed :TextView = findViewById(R.id.windSpeed)
        val tvRainPercent :TextView = findViewById(R.id.rainPercent)
        val tvSnowPercent :TextView = findViewById(R.id.snowPercent)
        val aqitv :TextView = findViewById(R.id.tvAQI)
        val tvAQIClk :TextView = findViewById(R.id.tvAQIClk)

        val outlinedTextField :TextInputLayout = findViewById(R.id.outlinedTextField)
        val btnGo :Button = findViewById(R.id.btnGo)

        val ivToday :ImageView = findViewById(R.id.ivToday)
        val ivTom :ImageView = findViewById(R.id.ivTom)
        val ivNext :ImageView = findViewById(R.id.ivNext)

         val sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE)
         val editor = sharedPreferences.edit()

         var city = sharedPreferences.getString("city","kolkata")
         //var city:String = outlinedTextField.editText?.text.toString()


         if (city=="kolkata"){
             outlinedTextField.hint = "Kolkata"
         }else{
             outlinedTextField.hint = city
         }

         //function to get data from API
         fun makeRequest(city: String) {
             // Instantiate the RequestQueue.
             val queue = Volley.newRequestQueue(this)
             val apiKey = getString(R.string.API_KEY)
             val url = "https://api.weatherapi.com/v1/forecast.json?key=$apiKey&q=$city&days=3&aqi=yes&alerts=yes"


             val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                     { response ->
                         val temp: Int = response.getJSONObject("current").getInt("temp_c")
                         val weather:String = response.getJSONObject("current").getJSONObject("condition").getString("text")
                         textView.text = "$temp \u2103"
                         tvWeather.text = "$weather"

                         val aqi  = response.getJSONObject("current").getJSONObject("air_quality").getInt("pm2_5")
                         tvAqi.text = "AOI $aqi"
                         aqitv.text = aqi.toString()

                         val  toDay :JSONObject= response.getJSONObject("forecast").getJSONArray("forecastday")[0] as JSONObject
                         val  tomorrow:JSONObject= response.getJSONObject("forecast").getJSONArray("forecastday")[1] as JSONObject
                         val  nextDay :JSONObject= response.getJSONObject("forecast").getJSONArray("forecastday")[2] as JSONObject

                         val maxTempToday:Double = toDay.getJSONObject("day").getDouble("maxtemp_c")
                         val minTempToday:Double = toDay.getJSONObject("day").getDouble("mintemp_c")
                         val textToday:String = toDay.getJSONObject("day").getJSONObject("condition").getString("text")
                         val urlToday:String = "https:"+toDay.getJSONObject("day").getJSONObject("condition").getString("icon")

                         val maxTempTom:Double = tomorrow.getJSONObject("day").getDouble("maxtemp_c")
                         val minTempTomorrow:Double = tomorrow.getJSONObject("day").getDouble("mintemp_c")
                         val textTomorrow:String = tomorrow.getJSONObject("day").getJSONObject("condition").getString("text")
                         val urlTomorrow:String = "https:"+tomorrow.getJSONObject("day").getJSONObject("condition").getString("icon")

                         val maxTempNext:Double = nextDay.getJSONObject("day").getDouble("maxtemp_c")
                         val minTempNext:Double = nextDay.getJSONObject("day").getDouble("mintemp_c")
                         val textNext:String = nextDay.getJSONObject("day").getJSONObject("condition").getString("text")
                         val urlNext:String = "https:"+nextDay.getJSONObject("day").getJSONObject("condition").getString("icon")

                         val sunrise = toDay.getJSONObject("astro").getString("sunrise")
                         val sunset = toDay.getJSONObject("astro").getString("sunset")
                         val dayObj = toDay.getJSONObject("day")
                         val rainPercent =dayObj.getString("daily_chance_of_rain")
                         val snowPercent =dayObj.getString("daily_chance_of_snow")
                         val humidity = dayObj.getInt("avghumidity")
                         val windSpeed = dayObj.getDouble("maxwind_kph")
                         val uvIndex = dayObj.getInt("uv")
                         val pressure = response.getJSONObject("current").getInt("pressure_mb")

                         setImage(ivToday,urlToday)
                         setImage(ivTom,urlTomorrow)
                         setImage(ivNext,urlNext)

                         tvToday.text = "Today - $textToday"
                         tvTom.text = "Tomorrow - $textTomorrow"
                         tvNext.text = "Day after - $textNext"

                         tvToday1.text = "$minTempToday\u00B0 / $maxTempToday\u00B0"
                         tvTom1.text = "$minTempTomorrow\u00B0 / $maxTempTom\u00B0"
                         tvNext1.text = "$minTempNext\u00B0 / $maxTempNext\u00B0"

                         tvSunrise.text = sunrise
                         tvSunSet.text = sunset
                         tvRainPercent.text = rainPercent
                         tvSnowPercent.text = snowPercent
                         tvHumidity.text = humidity.toString()
                         tvWindSpeed.text = windSpeed.toString()
                         tvUvIndex.text = uvIndex.toString()
                         tvPressure.text = pressure.toString()
                     },
                     { error ->
                         Toast.makeText(this, "Unable_to_load ", Toast.LENGTH_LONG).show()
                         Log.d("The_error_is", error.toString())
                     }
             )
// Add the request to the RequestQueue.
             queue.add(jsonObjectRequest)

         }

         btnGo.setOnClickListener {
             val inputText = outlinedTextField.editText?.text.toString()
             if (inputText ==""){
                 Toast.makeText(this,"Enter City",Toast.LENGTH_LONG).show()
                 return@setOnClickListener
             }
             makeRequest(inputText)
             editor.putString("city",inputText)
             editor.apply()
             outlinedTextField.hint = ""
         }
         city?.let { makeRequest(it) }

         tvAQIClk.setOnClickListener {
             AqiDialouge(this).show()
         }

     }



    private fun setImage(view: ImageView, url: String) {
                Glide
                    .with(view)
                    .load(url)
                    .placeholder(R.drawable.ic_cloud)
                    .into(view)
    }


}