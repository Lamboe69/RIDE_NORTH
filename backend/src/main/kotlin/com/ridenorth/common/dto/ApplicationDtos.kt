package com.ridenorth.common.dto

import com.ridenorth.module.application.ApplicationStatus
import com.ridenorth.module.driver.VehicleType
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.Instant

data class SubmitDriverApplicationRequest(
    @field:NotBlank(message = "Phone number is required")
    @field:Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    val phoneNumber: String,

    @field:NotBlank(message = "Full name is required")
    @field:Pattern(regexp = "^[a-zA-Z\\s]{2,100}$", message = "Full name must be 2-100 alphabetic characters")
    val fullName: String,

    @field:Pattern(regexp = "^[0-9]{10,20}$", message = "Invalid National ID number")
    val ninNumber: String?,

    @field:NotBlank(message = "Driving permit number is required")
    @field:Pattern(regexp = "^[A-Z0-9]{5,20}$", message = "Driving permit must be 5-20 alphanumeric characters")
    val licenseNumber: String,

    @field:NotNull(message = "Vehicle type is required")
    val vehicleType: VehicleType,

    @field:NotBlank(message = "Plate number is required")
    @field:Pattern(regexp = "^[A-Z0-9\\-]{3,15}$", message = "Invalid plate number format")
    val plateNumber: String,

    val make: String?,
    val model: String?,
    val year: String?,

    @field:Min(value = 1, message = "Capacity must be at least 1")
    @field:Max(value = 50, message = "Capacity cannot exceed 50")
    val capacity: Int = 1,

    val documents: String? = null
)

data class ReviewApplicationRequest(
    val rejectionReason: String? = null
)

data class DriverApplicationDto(
    val id: String,
    val applicationRef: String,
    val phoneNumber: String,
    val fullName: String,
    val ninNumber: String?,
    val licenseNumber: String,
    val vehicleType: VehicleType,
    val plateNumber: String,
    val make: String?,
    val model: String?,
    val year: String?,
    val capacity: Int,
    val documents: String?,
    val status: ApplicationStatus,
    val rejectionReason: String?,
    val submittedAt: Instant?,
    val reviewedAt: Instant?
)
