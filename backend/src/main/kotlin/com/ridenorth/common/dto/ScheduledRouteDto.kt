package com.ridenorth.common.dto

import com.ridenorth.module.scheduled.RouteStatus
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.Instant

data class CreateScheduledRouteDto(
    @field:NotBlank(message = "Origin is required")
    val origin: String,

    @field:NotBlank(message = "Destination is required")
    val destination: String,

    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Origin latitude is required")
    val originLatitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Origin longitude is required")
    val originLongitude: Double,

    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Destination latitude is required")
    val destinationLatitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Destination longitude is required")
    val destinationLongitude: Double,

    @field:NotNull(message = "Departure time is required")
    val departureTime: Instant,

    @field:Min(value = 1, message = "Seat capacity must be at least 1")
    @field:Max(value = 100, message = "Seat capacity cannot exceed 100")
    val seatCapacity: Int,

    @field:Positive(message = "Price per seat must be greater than 0")
    val pricePerSeat: Double,

    @field:Min(value = 0, message = "Minimum seats to confirm cannot be negative")
    val minSeatsToConfirm: Int = 0,

    val notes: String? = null
)

data class ScheduledRouteDto(
    val id: String,
    val operatorId: String,
    val origin: String,
    val destination: String,
    val departureTime: Instant,
    val seatCapacity: Int,
    val seatsBooked: Int,
    val pricePerSeat: Double,
    val minSeatsToConfirm: Int,
    val status: RouteStatus,
    val notes: String?,
    val createdAt: Instant
)
