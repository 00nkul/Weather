package com.example.weather

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat


class AqiDialouge(context: Context) :Dialog(context) {
    init {
        this.setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.aqi_dialouge)
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
        var city = sharedPreferences.getString("city","kolkata")

        val tvAqiMain:TextView = findViewById(R.id.tvAqiMain)
        val tvPm2:TextView = findViewById(R.id.tvPm2)
        val tvPm10:TextView = findViewById(R.id.tvPm10)
        val tvSo2:TextView = findViewById(R.id.tvSo2)
        val tvNO2:TextView = findViewById(R.id.tvNO2)
        val tvCO:TextView = findViewById(R.id.tvCO)
        val tvO3:TextView = findViewById(R.id.tvO3)

        val queue = Volley.newRequestQueue(context)

        val apiKey = context.getString(R.string.API_KEY)
        val url = "https://api.weatherapi.com/v1/forecast.json?key=$apiKey&q=$city&days=3&aqi=yes&alerts=yes"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val aqiObj  = response.getJSONObject("current").getJSONObject("air_quality")

                    val pm2 = roundOffDecimal(aqiObj.getDouble("pm2_5"))
                    val pm10 = roundOffDecimal(aqiObj.getDouble("pm10"))
                    val co = roundOffDecimal(aqiObj.getDouble("co"))
                    val so2 = roundOffDecimal(aqiObj.getDouble("so2"))
                    val no2 = roundOffDecimal(aqiObj.getDouble("no2"))
                    val o3 = roundOffDecimal(aqiObj.getDouble("o3"))

                    tvAqiMain.text = pm2
                    tvPm10.text = pm10
                    tvPm2.text = pm2
                    tvSo2.text = so2
                    tvCO.text = co
                    tvO3.text = o3
                    tvNO2.text = no2
                },
                { error ->
                    Toast.makeText(context, "Unable_to_load_AQI", Toast.LENGTH_LONG).show()
                    Log.d("The_error_is", error.toString())
                }
        )

        queue.add(jsonObjectRequest)
    }

    fun roundOffDecimal(number: Double): String? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toString()
    }
}