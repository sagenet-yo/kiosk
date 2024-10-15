package com.example.kiosk

import com.example.kiosk.ApiService.AdminApiService
import com.example.kiosk.ApiService.EmployeeApiService
import com.example.kiosk.ApiService.VisitorApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.253.40.118:8080"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create instances of each API service
    val visitorApi: VisitorApiService = retrofit.create(VisitorApiService::class.java)
    val employeeApi: EmployeeApiService = retrofit.create(EmployeeApiService::class.java)
    val adminApi: AdminApiService = retrofit.create(AdminApiService::class.java)
}