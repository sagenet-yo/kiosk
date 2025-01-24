package com.example.kiosk.Dto

import java.util.UUID

data class DeviceDto(
    val id: UUID?,
    val location: String,
    val username: String,
    val password: String,
    val deliveryEmail: String
)