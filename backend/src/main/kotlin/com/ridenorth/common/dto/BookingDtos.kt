package com.ridenorth.common.dto

import com.ridenorth.module.booking.RideRequestStatus
import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.booking.TripStatus
import java.time.Instant

data class CreateRideRequestDto(
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val dropoffLatitude: Double,
    val dropoffLongitude: Double,
    val pickupAddress: String,
    val dropoffAddress: String,
    val vehicleType: VehicleType,
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
    val rideRequestId: String
)

data class StartTripRequest(
    val tripId: String
)

data class CompleteTripRequest(
    val tripId: String
)
