package com.ridenorth.common.dto

import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.driver.KycStatus
import java.time.Instant

data class RegisterDriverDto(
    val phoneNumber: String,
    val name: String,
    val licenseNumber: String,
    val vehicleType: VehicleType,
    val plateNumber: String,
    val vehicleCapacity: Int = 1
)

data class UpdateDriverLocationDto(
    val latitude: Double,
    val longitude: Double
)

data class UpdateOnlineStatusDto(
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
