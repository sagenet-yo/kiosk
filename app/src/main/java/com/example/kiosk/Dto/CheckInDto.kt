package com.example.kiosk.Dto


data class CheckInDto (
    val employeeEmail: String,
    val location: String,
    val visitor: VisitorDto,
)