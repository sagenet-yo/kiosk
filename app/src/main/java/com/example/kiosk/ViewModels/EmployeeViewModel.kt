import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kiosk.Dto.EmployeeSearchResponse
import com.example.kiosk.Entities.Employee
import com.example.kiosk.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeViewModel : ViewModel() {

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees

    private var isFetching = false

    init {
        fetchEmployees()
    }

    // Fetch all employees initially
    private fun fetchEmployees() {
        if (isFetching) return
        isFetching = true

        val apiService = RetrofitClient.employeeApi
        val call = apiService.listAllEmployees()

        call.enqueue(object : Callback<List<Employee>> {
            override fun onResponse(
                call: Call<List<Employee>>,
                response: Response<List<Employee>>
            ) {
                if (response.isSuccessful) {
                    val employeeList = response.body() ?: emptyList()
                    _employees.value = employeeList
                    Log.d("EmployeeViewModel", "Fetched employees: $employeeList")
                } else {
                    val errorMessage = response.message()
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()

                    Log.e(
                        "EmployeeViewModel",
                        "Error fetching employees. Status code: $statusCode, Message: $errorMessage, Error body: $errorBody"
                    )
                }
                isFetching = false
            }

            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                Log.e("EmployeeViewModel", "API call failed: ${t.message}")
                isFetching = false
            }
        })
    }

    // Search employees based on search term
    fun searchEmployees(searchTerm: String) {
        if (searchTerm.isBlank()) {
            fetchEmployees()
            return
        }

        val apiService = RetrofitClient.employeeApi
        val call = apiService.searchEmployee(searchTerm)

        call.enqueue(object : Callback<List<EmployeeSearchResponse>> {
            override fun onResponse(
                call: Call<List<EmployeeSearchResponse>>,
                response: Response<List<EmployeeSearchResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("EmployeeViewModel", "API Response: $responseBody")
                    _employees.value = responseBody?.map { responseItem ->
                        Employee(
                            responseItem.id,
                            responseItem.firstName,
                            responseItem.lastName,
                            responseItem.email ?: "N/A"
                        )
                    } ?: emptyList()
                } else {
                    Log.e("EmployeeViewModel", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<EmployeeSearchResponse>>, t: Throwable) {
                Log.e("EmployeeViewModel", "API call failed: ${t.message}")
            }
        })
    }
}