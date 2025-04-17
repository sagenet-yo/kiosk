package com.example.kiosk.Screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton

@Composable
fun ThreeOptionHomeScreen(
    navigationBack: () -> Unit,
    navigationToThreeOptionDeliveryScreen: () -> Unit,
    navigationToThreeOptionCheckOutScreen: () -> Unit,
    navigationToEmployeeListScreen: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(20.dp))

        Image(
            painter = painterResource(id = R.drawable.sagenet_logo),
            contentDescription = "",
            modifier = Modifier
                .width( 900.dp) // Adjust width as needed
                .padding(16.dp)
                .height(300.dp) // Adjust height as needed

        )



        Text(
            text = "Welcome, please select the reason for your visit.",
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )

        Spacer(modifier = Modifier.padding(12.dp))

        val context = LocalContext.current

        // Launcher to request permissions
        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Handle permission result
            val granted = permissions.entries.all { it.value == true }
            if (granted) {
                // Permissions granted, navigate to employee list
                navigationToEmployeeListScreen()
            } else {
                // Handle the case where permissions are denied
                // Show a dialog or error message
            }
        }

        // Permissions needed
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }


        val scrollState = rememberScrollState()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    navigationToEmployeeListScreen()
//                    requestPermissionLauncher.launch(requiredPermissions)
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White// Custom text color
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Login,
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp)
                    )

                    Text("Check In")
                }
            }

            Spacer(modifier = Modifier.padding(32.dp))

            Button(
                onClick = {
                    navigationToThreeOptionCheckOutScreen()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White// Custom text color
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp)
                    )

                    Text("Check Out")
                }
            }

            Spacer(modifier = Modifier.padding(32.dp))

            Button(
                onClick = { navigationToThreeOptionDeliveryScreen() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalShipping,
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp)
                    )

                    Text("Delivery")
                }
            }
        }
    }
}