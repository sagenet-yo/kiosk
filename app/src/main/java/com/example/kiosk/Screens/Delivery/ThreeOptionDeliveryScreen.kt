package com.example.kiosk.Screens.Delivery

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton

@Composable
fun ThreeOptionDeliveryScreen(navigationBack: ()->Unit, navigationToDeliveryEndScreen: ()->Unit) {

    ExitButton(navigationBack)

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.sagenet_logo),
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )

        Text(
            text = "Select a message to send for this delivery:",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.padding(32.dp))

        Button(
            onClick = {navigationToDeliveryEndScreen()},

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
            onClick = {navigationToDeliveryEndScreen()},

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
            onClick = {navigationToDeliveryEndScreen()},

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