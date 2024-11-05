package com.example.bottomnavigationhw1.ui.weather.currentWeather.utils

import com.example.bottomnavigationhw1.ui.weather.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Util.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}