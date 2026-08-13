package com.ridenorth.common.dto

import com.ridenorth.module.booking.RideRequestStatus
import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.booking.TripStatus
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.Instant

data class CreateRideRequestDto(
    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Pickup latitude is required")
    val pickupLatitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Pickup longitude is required")
    val pickupLongitude: Double,

    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Dropoff latitude is required")
    val dropoffLatitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Dropoff longitude is required")
    val dropoffLongitude: Double,

    @field:NotBlank(message = "Pickup address is required")
    val pickupAddress: String,

    @field:NotBlank(message = "Dropoff address is required")
    val dropoffAddress: String,

    @field:NotNull(message = "Vehicle type is required")
    val vehicleType: VehicleType,

    @field:Min(value = 1, message = "Passenger count must be at least 1")
    @field:Max(value = 50, message = "Passenger count cannot exceed 50")
    val passengerCount: Int = 1,

    val notes: String? = null
)

data class RideRequestDto(
    val id: String,
    val riderId: String,
    val pickupLocation: LocationDto,
    val dropoffLocation: LocationDto,
    val pickupAddress: String,
    val dropoffAddress: String,
    val vehicleType: VehicleType,
    val passengerCount: Int,
    val fareEstimate: Double,
    val surgeMultiplier: Double,
    val status: RideRequestStatus,
    val expiresAt: Instant?,
    val createdAt: Instant?
)

data class TripDto(
    val id: String,
    val rideRequestId: String,
    val riderId: String,
    val driverId: String,
    val startedAt: Instant?,
    val endedAt: Instant?,
    val distanceKm: Double,
    val durationMinutes: Int,
    val finalFare: Double,
    val commissionAmount: Double,
    val driverEarnings: Double,
    val status: TripStatus,
    val paymentMethod: String,
    val createdAt: Instant?
)

data class AcceptTripRequest(
    @field:NotBlank(message = "Ride request ID is required")
    val rideRequestId: String
)

data class StartTripRequest(
    @field:NotBlank(message = "Trip ID is required")
    val tripId: String
)

data class CompleteTripRequest(
    @field:NotBlank(message = "Trip ID is required")
    val tripId: String
)
