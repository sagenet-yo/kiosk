package com.example.kiosk.ApiService

import com.example.kiosk.Dto.AdminDto
import com.example.kiosk.Entities.Admin
import retrofit2.Call
import retrofit2.http.*
import java.util.UUID

interface AdminApiService {

    @POST("api/v1/admins/create")
    fun createAdmin(@Body dto: AdminDto): Call<AdminDto>

    @PUT("api/v1/admins/update")
    fun updateAdmin(@Body dto: AdminDto): Call<AdminDto>

    @GET("api/v1/admins/listAll")
    fun listAllAdmins(): Call<List<Admin>>

    @DELETE("api/v1/admins/{id}/delete")
    fun deleteAdmin(@Path("id") uuid: UUID): Call<AdminDto>

    @GET("api/v1/admins/{username}/{password}/login")
    fun login(@Path("username") username: String, @Path("password") password: String): Call<Boolean>
}