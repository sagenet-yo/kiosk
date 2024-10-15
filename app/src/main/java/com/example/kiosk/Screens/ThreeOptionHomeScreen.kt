package com.example.kiosk.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton

@Composable
fun ThreeOptionHomeScreen(navigationBack: ()->Unit, navigationToThreeOptionDeliveryScreen: ()->Unit, navigationToEmployeeListScreen: ()->Unit){

    ExitButton(navigationBack)

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally){

        Image(
            painter = painterResource(id = R.drawable.sagenet_logo),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )

        Text(text = "Please select the reason for your visit.",
            textAlign = TextAlign.Center,
            fontSize = 25.sp)

        Spacer(modifier = Modifier.padding(96.dp))

        Row(verticalAlignment = Alignment.CenterVertically){

            Button(onClick = {navigationToEmployeeListScreen()},
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White// Custom text color
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
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

            Button(onClick = {},
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White// Custom text color
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
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

            Button(onClick = {navigationToThreeOptionDeliveryScreen()},
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
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