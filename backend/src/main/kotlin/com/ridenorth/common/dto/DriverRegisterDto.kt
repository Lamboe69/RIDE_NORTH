package com.ridenorth.common.dto

data class RegisterDriverRequest(
    val phoneNumber: String,
    val name: String,
    val otp: String,
    val licenseNumber: String,
    val vehicleType: com.ridenorth.module.driver.VehicleType,
    val plateNumber: String,
    val vehicleCapacity: Int = 1
)
