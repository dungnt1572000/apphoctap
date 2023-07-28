package com.example.apphoctap.api

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val baseUrl = "https://opentdb.com/"
    private val retrofit: Retrofit
        get() {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(
                RxJava3CallAdapterFactory.create()
            ).build()
        }

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}