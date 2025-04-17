package com.example.kiosk.ViewModels

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiosk.ApiService.DeviceApiService
import com.example.kiosk.Dto.DeviceDto
import com.example.kiosk.Dto.LoginRequest
import com.example.kiosk.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModel(
    private val context: Context
) : ViewModel() {
    var loginSuccess = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun authenticate(loginRequest: LoginRequest) {
        val apiService = RetrofitClient.deviceApi
        val call = apiService.authenticate(loginRequest)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    // Save login state
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                    loginSuccess.value = true

                    apiService.findByUsername(loginRequest.username).enqueue(object : Callback<DeviceDto> {
                        override fun onResponse(call: Call<DeviceDto>, response: Response<DeviceDto>) {
                            if (response.isSuccessful) {
                                val device = response.body()
                                if (device != null) {
                                    sharedPreferences.edit().putString("deliveryEmail", device.deliveryEmail).apply()
                                    sharedPreferences.edit().putString("location", device.location).apply()
                                }
                            }
                        }

                        override fun onFailure(call: Call<DeviceDto>, t: Throwable) {
                            errorMessage.value = "Error fetching device info: ${t.message}"
                        }
                    })
                } else {
                    errorMessage.value = "Authentication failed: Invalid credentials"
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                errorMessage.value = "Unexpected error: ${t.message}"
            }
        })
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
}