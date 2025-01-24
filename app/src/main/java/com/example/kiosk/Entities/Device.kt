package com.example.kiosk.Entities

import java.util.UUID

data class Device (
    val id: UUID,
    val location: String,
    val username: String,
    val password: String,
    val deliveryEmail: String
)