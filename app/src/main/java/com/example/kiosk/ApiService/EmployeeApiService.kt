package com.example.kiosk.ApiService

import com.example.kiosk.Dto.EmployeeDto
import com.example.kiosk.Dto.EmployeeSearchResponse
import com.example.kiosk.Entities.Employee
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface EmployeeApiService {

    @POST("api/v1/employees/create")
    fun createEmployee(@Body dto: EmployeeDto): Call<EmployeeDto>

    @GET("api/v1/employees/{searchTerm}/search")
    fun searchEmployee(@Path("searchTerm") searchTerm: String): Call<List<EmployeeSearchResponse>>

    @PUT("api/v1/employees/update")
    fun updateEmployee(@Body dto: EmployeeDto): Call<EmployeeDto>

    @GET("api/v1/employees/listAll")
    fun listAllEmployees(): Call<List<Employee>>

    @DELETE("api/v1/employees/{uuid}/delete")
    fun deleteEmployee(@Path("uuid") uuid: UUID): Call<EmployeeDto>

    @POST("api/v1/employees/{uuid}/email")
    fun emailEmployee(@Path("uuid") uuid: UUID, @Body message: String): Call<EmployeeDto>
}
