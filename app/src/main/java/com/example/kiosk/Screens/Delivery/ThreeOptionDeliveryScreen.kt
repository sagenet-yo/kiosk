

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton
import com.example.kiosk.RetrofitClient
import com.example.kiosk.Dto.EmailRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ThreeOptionDeliveryScreen(navigationBack: () -> Unit, navigationToDeliveryEndScreen: () -> Unit) {

    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val email = sharedPreferences.getString("deliveryEmail", null)
    val location = sharedPreferences.getString("location", "none")
    val deviceApiService = RetrofitClient.deviceApi

    ExitButton(navigationBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sagenet_logo),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )

        Text(
            text = "Select a message to send for this delivery:",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email != null) {
                    val emailRequest = EmailRequest("Package Requires Signature at $location Office Lobby", "Someone requires a signature for a delivery at the $location office lobby near the check-in kiosk.")
                    deviceApiService.deliveryEmail(email, emailRequest)
                        .enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful) {
                                    Log.i("API_SUCCESS", "Email sent successfully")
                                } else {
                                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("API_FAILURE", "Failure: ${t.message}")
                            }
                        })
                } else {
                    Log.e("NULL_CHECK", "Email is null")
                }
                navigationToDeliveryEndScreen()
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.White,
            ),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.5.dp, Color.Black),
            modifier = Modifier
                .width(1000.dp)
                .height(100.dp)
                .padding(16.dp)
        ) {
            Text(
                text = "Requires signature",
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }

        Button(
            onClick = {
                if (email != null) {
                    val emailRequest = EmailRequest("Package Delivered to $location Office Lobby", "A package has been left in the $location office lobby near the check-in kiosk.")
                    deviceApiService.deliveryEmail(email, emailRequest)
                        .enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful) {
                                    Log.i("API_SUCCESS", "Email sent successfully")
                                } else {
                                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("API_FAILURE", "Failure: ${t.message}")
                            }
                        })
                } else {
                    Log.e("NULL_CHECK", "Email is null")
                }
                navigationToDeliveryEndScreen()
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.White,
            ),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.5.dp, Color.Black),
            modifier = Modifier
                .width(1000.dp)
                .height(100.dp)
                .padding(16.dp)
        ) {
            Text(
                text = "Left delivery at the counter",
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }

        Button(
            onClick = {
                if (email != null) {
                    val emailRequest = EmailRequest("Package Will be Redelivered to $location Office Lobby", "A package will be redelivered to $location office lobby")
                    deviceApiService.deliveryEmail(email, emailRequest)
                        .enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.isSuccessful) {
                                    Log.i("API_SUCCESS", "Email sent successfully")
                                } else {
                                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.e("API_FAILURE", "Failure: ${t.message}")
                            }
                        })
                } else {
                    Log.e("NULL_CHECK", "Email is null")
                }
                navigationToDeliveryEndScreen()
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.White,
            ),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(0.5.dp, Color.Black),
            modifier = Modifier
                .width(1000.dp)
                .height(100.dp)
                .padding(16.dp)
        ) {
            Text(
                text = "Will redeliver",
                fontWeight = FontWeight.Light,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}