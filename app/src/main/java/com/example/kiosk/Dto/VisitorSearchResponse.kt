package com.example.kiosk.Dto

import android.graphics.Bitmap
import java.util.UUID


data class VisitorSearchResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val checkedIn: Boolean,
    val picture: ByteArray,
    val personOfInterest: UUID,
    val checkInTime : String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisitorSearchResponse

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (checkedIn != other.checkedIn) return false
        if (personOfInterest != other.personOfInterest) return false
        if (checkInTime != other.checkInTime) return false
        return picture.contentEquals(other.picture)

    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + checkedIn.hashCode()
        result = 31 * result + personOfInterest.hashCode()
        result = 31 * result + picture.contentHashCode()
        result = 31 * result + checkInTime.hashCode()
        return result
    }
}