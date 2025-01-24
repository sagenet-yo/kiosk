package com.example.kiosk.Screens

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.kiosk.Dto.DeviceDto
import com.example.kiosk.R
import com.example.kiosk.RetrofitClient
import java.util.UUID
import androidx.compose.ui.platform.LocalContext

@Composable
fun SetupScreen() {

    var location by remember { mutableStateOf("") }
    var deliveryEmail by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current // Get the context here

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.sagenet_logo),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = deliveryEmail,
            onValueChange = { deliveryEmail = it },
            label = { Text("Delivery Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.isNotEmpty() && newPassword.isEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.isNotEmpty() && confirmPassword.isEmpty()
        )

        Button(
            onClick = {
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "Password fields cannot be empty"
                } else if (newPassword != confirmPassword) {
                    errorMessage = "Passwords do not match"
                } else if (newPassword.length < 6) {
                    errorMessage = "Password should be at least 6 characters"
                } else {
                    errorMessage = ""
                    val apiService = RetrofitClient.deviceApi
                    val call = apiService.create(DeviceDto(UUID.randomUUID(), location, username, newPassword, deliveryEmail))

                    // Access shared preferences with the context
                    val sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username) // Use the username entered by the user
                    editor.putString("location", location) // Use the location entered by the user
                    editor.putString("deliveryEmail", deliveryEmail) // Use the email entered by the user
                    editor.apply()

                    (context as Activity).finish()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.DarkGray
            )
        ) {
            Text("Submit")
        }
    }
}
