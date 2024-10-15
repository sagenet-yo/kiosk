package com.example.kiosk.Dto

import java.util.UUID

data class AdminDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String
)