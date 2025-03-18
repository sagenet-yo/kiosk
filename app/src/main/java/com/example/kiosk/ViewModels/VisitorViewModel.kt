package com.example.kiosk.ViewModels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kiosk.Dto.VisitorDto
import com.example.kiosk.RetrofitClient.visitorApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

class VisitorViewModel : ViewModel() {
    private val _visitors = mutableStateListOf<VisitorDto>()
    val visitors: List<VisitorDto> get() = _visitors

    fun fetchVisitors() {
        viewModelScope.launch {
            getCheckedInVisitorList { visitorList ->
                visitorList?.let { checkedInVisitors ->
                    _visitors.clear()
                    _visitors.addAll(checkedInVisitors)
                    Log.d("VisitorViewModel", "Checked-in visitors: $checkedInVisitors")
                }
            }
        }
    }

    fun getVisitorList(callback: (List<VisitorDto>?) -> Unit) {
        visitorApi.listAllVisitors().enqueue(object : Callback<List<VisitorDto>> {
            override fun onResponse(call: Call<List<VisitorDto>>, response: Response<List<VisitorDto>>) {
                if (response.isSuccessful) {
                    callback(response.body()) // Pass the entire list to the callback
                } else {
                    Log.e("VisitorListApi", "Error fetching visitors: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<VisitorDto>>, t: Throwable) {
                Log.e("VisitorListApi1", "API call failed: $t")
                callback(null)
            }
        })
    }

    fun getCheckedInVisitorList(callback: (List<VisitorDto>?) -> Unit) {
        visitorApi.listCheckedInVisitors().enqueue(object : Callback<List<VisitorDto>> {
            override fun onResponse(call: Call<List<VisitorDto>>, response: Response<List<VisitorDto>>) {
                if (response.isSuccessful) {
                    callback(response.body()) // Pass the entire list to the callback
                } else {
                    Log.e("VisitorListApi", "Error fetching visitors: ${response.code()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<VisitorDto>>, t: Throwable) {
                Log.e("VisitorListApi1", "API call failed: $t")
                callback(null)
            }
        })
    }
}
