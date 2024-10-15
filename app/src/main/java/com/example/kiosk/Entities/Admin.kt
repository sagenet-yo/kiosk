package com.example.kiosk.Entities

import java.util.UUID

data class Admin(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String
)