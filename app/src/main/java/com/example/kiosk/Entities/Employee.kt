package com.example.kiosk.Entities

import java.util.UUID


data class Employee(
val id: UUID,
val firstName: String,
val lastName: String,
val email: String,
val mailGroup: Boolean
)
