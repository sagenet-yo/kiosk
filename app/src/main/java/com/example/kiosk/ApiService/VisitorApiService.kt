package com.example.kiosk.ApiService
import com.example.kiosk.Dto.CheckInDto
import com.example.kiosk.Dto.EmailRequest
import com.example.kiosk.Dto.VisitorDto
import com.example.kiosk.Dto.VisitorSearchResponse
import com.example.kiosk.Entities.Device
import com.example.kiosk.Entities.Employee
import com.example.kiosk.Entities.Visitor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface VisitorApiService {
    @POST("api/v1/visitors/create")
    fun create(@Body dto: VisitorDto): Call<VisitorDto>

    @GET("api/v1/visitors/{searchTerm}/search")
    fun searchVisitors(@Path("searchTerm") searchTerm: String): Call<List<VisitorSearchResponse>>

    @GET("api/v1/visitors/listAll")
    fun listAllVisitors(): Call<List<VisitorDto>>

    @GET("api/v1/visitors/listCheckedIn")
    fun listCheckedInVisitors(): Call<List<VisitorDto>>

    @PUT("api/v1/visitors/update")
    fun update(@Body dto: VisitorDto): Call<VisitorDto>

    @DELETE("api/v1/visitors/{uuid}/delete")
    fun delete(@Path("uuid") uuid: UUID): Call<VisitorDto>

    @POST("api/v1/visitors/{uuid}/checkOutVisitor")
    fun checkOutVisitor(@Path("uuid") uuid: UUID): Call<VisitorDto>

    @POST("api/v1/visitors/checkInEmail")
    fun checkInEmail(@Body checkInDto: CheckInDto): Call<String>

    @POST("api/v1/visitors/generateLabel")
    fun generateLabel(@Body visitorDto: VisitorDto): Call<ResponseBody>


}