package com.example.kiosk.RepeatButtons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExitButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)  // Adds padding around the Box
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = Modifier
                .align(Alignment.TopEnd)  // Aligns the button to the top-right corner
                .size(40.dp)  // Optional: Set size for the button
        ) {
            Icon(
                imageVector = Icons.Filled.Close,  // Use the 'Close' icon from Material Icons
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}