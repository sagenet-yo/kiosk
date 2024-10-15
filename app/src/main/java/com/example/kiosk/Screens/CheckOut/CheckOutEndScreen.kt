package com.example.kiosk.Screens.CheckOut

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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

@Composable
fun CheckOutEndScreen(){
    Column {
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)) {

            Spacer(modifier = Modifier.weight(1.0f))

            Image(
                painter = painterResource(id = R.drawable.sagenet_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp),
                alignment = Alignment.Center
            )

            Spacer(modifier = Modifier.weight(0.75f))

            Button(
                onClick = {},

                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.DarkGray,
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, Color.Black),
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp)
                    .wrapContentSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Next Visitor"
                )

                Spacer(modifier = Modifier.padding(2.dp))

                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )


            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 196.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Thank You!",
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "You've been checked out.",
                fontSize = 60.sp,
                fontWeight = FontWeight.Light)
        }
    }
}