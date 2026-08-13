package com.ridenorth.common.dto

import com.ridenorth.module.user.UserRole
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class PhoneOtpRequest(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    val phoneNumber: String
)

data class VerifyOtpRequest(
    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String,
    @field:NotBlank(message = "OTP is required")
    val otp: String
)

data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: UserDto
)

data class UserDto(
    val id: String,
    val phoneNumber: String,
    val name: String,
    val role: UserRole,
    val ratingAvg: Double,
    val ratingCount: Int,
    val preferredLanguage: String,
    val isActive: Boolean
)

data class DriverProfileDto(
    val userId: String,
    val licenseNumber: String,
    val kycStatus: String,
    val isOnline: Boolean,
    val currentLocation: LocationDto?,
    val totalTrips: Int,
    val acceptanceRate: Double
)

data class LocationDto(
    val latitude: Double,
    val longitude: Double
)
