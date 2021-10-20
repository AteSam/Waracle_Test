package com.example.waracleandroidtest.data

import com.example.waracleandroidtest.data.model.Cake
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CakeService {

    @GET("t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client")
    suspend fun getCakeResponse(): Response<List<Cake>>

    companion object {
        fun buildService(endpoint:String,
                         okHttpClient: OkHttpClient,
                         retrofit: Retrofit
        ):CakeService = retrofit
            .newBuilder()
            .addConverterFactory(
                GsonConverterFactory.create())
            .baseUrl(endpoint)
            .client(okHttpClient)
            .build()
            .create(CakeService::class.java)
    }
}