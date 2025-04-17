package com.example.kiosk.Dto

import java.util.UUID


data class VisitorDto(
    val id: UUID?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val checkedIn: Boolean,
    val company: String,
    val phoneNumber: String,
    val picture: String,
    val personOfInterest: String,
    val checkInTime : String,
    val checkOutTime : String,
    val location : String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisitorDto

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (checkedIn != other.checkedIn) return false
        if (company != other.company) return false
        if (phoneNumber != other.phoneNumber) return false
        if (personOfInterest != other.personOfInterest) return false
        if (checkInTime != other.checkInTime) return false
        if (checkOutTime != other.checkOutTime) return false
        if (location != other.location) return false
        return picture.contentEquals(other.picture)
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + checkedIn.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + personOfInterest.hashCode()
        result = 31 * result + picture.hashCode()
        result = 31 * result + checkInTime.hashCode()
        result = 31 * result + checkOutTime.hashCode()
        result = 31 * result + location.hashCode()
        return result
    }
}
