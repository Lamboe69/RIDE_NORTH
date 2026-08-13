package com.ridenorth.common.dto

import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.driver.KycStatus
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import java.time.Instant

data class RegisterDriverRequest(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    val phoneNumber: String,

    @field:NotBlank(message = "Name is required")
    @field:Pattern(regexp = "^[a-zA-Z\\s]{2,50}$", message = "Name must be 2-50 alphabetic characters")
    val name: String,

    @field:NotBlank(message = "OTP is required")
    @field:Pattern(regexp = "^[0-9]{6}$", message = "OTP must be exactly 6 digits")
    val otp: String,

    @field:NotBlank(message = "License number is required")
    @field:Pattern(regexp = "^[A-Z0-9]{5,20}$", message = "License number must be 5-20 alphanumeric characters")
    val licenseNumber: String,

    @field:NotNull(message = "Vehicle type is required")
    val vehicleType: VehicleType,

    @field:NotBlank(message = "Plate number is required")
    @field:Pattern(regexp = "^[A-Z0-9\\-]{3,15}$", message = "Invalid plate number format")
    val plateNumber: String,

    @field:Min(value = 1, message = "Capacity must be at least 1")
    @field:Max(value = 50, message = "Capacity cannot exceed 50")
    val vehicleCapacity: Int = 1
)

data class UpdateDriverLocationDto(
    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Latitude is required")
    val latitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Longitude is required")
    val longitude: Double
)

data class UpdateOnlineStatusDto(
    @field:NotNull(message = "Online status is required")
    val isOnline: Boolean
)

data class VehicleDto(
    val id: String,
    val type: VehicleType,
    val plateNumber: String,
    val capacity: Int,
    val isVerified: Boolean,
    val photos: String?,
    val isActive: Boolean
)

data class DriverKycUpdateDto(
    val kycStatus: KycStatus,
    val verificationDocs: String?
)
