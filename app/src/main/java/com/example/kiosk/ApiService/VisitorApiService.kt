package com.example.kiosk.ApiService
import com.example.kiosk.Dto.VisitorDto
import com.example.kiosk.Dto.VisitorSearchResponse
import com.example.kiosk.entities.Visitor
import retrofit2.Call
import retrofit2.http.*
import java.util.UUID

interface VisitorApiService {
    @POST("/api/v1/visitors/create")
    fun createVisitor(@Body dto: VisitorDto): Call<VisitorDto>

    @GET("/api/v1/visitors/{searchTerm}/search")
    fun searchVisitors(@Path("searchTerm") searchTerm: String): Call<List<VisitorSearchResponse>>

    @GET("/api/v1/visitors/listAll")
    fun listAllVisitors(): Call<List<Visitor>>

    @PUT("/api/v1/visitors/update")
    fun updateVisitor(@Body dto: VisitorDto): Call<VisitorDto>

    @DELETE("/api/v1/visitors/{id}/delete")
    fun deleteVisitor(@Path("id") uuid: UUID): Call<VisitorDto>

    @PUT("/api/v1/visitors/{id}/checkOut")
    fun checkOutVisitor(@Path("id") uuid: UUID): Call<VisitorDto>
}