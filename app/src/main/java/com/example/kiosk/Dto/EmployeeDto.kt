package com.example.kiosk.Dto

import java.util.UUID

data class EmployeeDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
)