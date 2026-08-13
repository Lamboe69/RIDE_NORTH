package com.ridenorth.common.dto

import com.ridenorth.module.scheduled.RouteStatus
import java.time.Instant

data class CreateScheduledRouteDto(
    val origin: String,
    val destination: String,
    val originLatitude: Double,
    val originLongitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val departureTime: Instant,
    val seatCapacity: Int,
    val pricePerSeat: Double,
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
