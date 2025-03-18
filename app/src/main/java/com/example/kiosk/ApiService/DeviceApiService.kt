package com.example.kiosk.ApiService

import com.example.kiosk.Dto.DeviceDto
import com.example.kiosk.Dto.EmailRequest
import com.example.kiosk.Dto.EmployeeDto
import com.example.kiosk.Dto.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceApiService {
    @POST("/api/v1/devices/create")
    fun create(@Body dto: DeviceDto): Call<DeviceDto>

    @POST("/api/v1/devices/authenticate")
    fun authenticate(@Body loginRequest: LoginRequest): Boolean

    @POST("api/v1/devices/{email}/deliveryEmail")
    fun deliveryEmail(@Path("email") email: String, @Body emailRequest : EmailRequest): Call<String>
}