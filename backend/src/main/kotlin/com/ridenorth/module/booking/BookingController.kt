package com.ridenorth.module.booking

import com.ridenorth.common.dto.*
import com.ridenorth.module.booking.TripRepository
import jakarta.validation.Valid
import org.locationtech.jts.geom.Point
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(private val bookingService: BookingService, private val tripRepository: TripRepository) {

    @PostMapping("/rides")
    @PreAuthorize("hasRole('RIDER')")
    fun createRideRequest(@Valid @RequestBody request: CreateRideRequestDto): ResponseEntity<RideRequestDto> {
        val saved = bookingService.createRideRequest(request)
        return ResponseEntity.ok(toDto(saved))
    }

    @PostMapping("/rides/{rideRequestId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    fun acceptRideRequest(@PathVariable rideRequestId: UUID): ResponseEntity<TripDto> {
        val trip = bookingService.acceptRideRequest(rideRequestId)
        return ResponseEntity.ok(toTripDto(trip))
    }

    @PostMapping("/trips/{tripId}/start")
    @PreAuthorize("hasRole('DRIVER')")
    fun startTrip(@PathVariable tripId: UUID): ResponseEntity<TripDto> {
        val trip = bookingService.startTrip(tripId)
        return ResponseEntity.ok(toTripDto(trip))
    }

    @PostMapping("/trips/{tripId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    fun completeTrip(@PathVariable tripId: UUID): ResponseEntity<TripDto> {
        val trip = bookingService.completeTrip(tripId)
        return ResponseEntity.ok(toTripDto(trip))
    }

    @GetMapping("/trips")
    @PreAuthorize("hasAnyRole('RIDER', 'DRIVER')")
    fun getMyTrips(): ResponseEntity<List<TripDto>> {
        val trips = bookingService.getMyTrips()
        return ResponseEntity.ok(trips.map { toTripDto(it) })
    }

    @GetMapping("/trips/{tripId}")
    fun getTripById(@PathVariable tripId: UUID): ResponseEntity<TripDto> {
        val trip = bookingService.getTripById(tripId)
        return ResponseEntity.ok(toTripDto(trip))
    }

    @PostMapping("/trips/{tripId}/cancel")
    fun cancelTrip(@PathVariable tripId: UUID): ResponseEntity<TripDto> {
        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found") }
        trip.status = TripStatus.CANCELLED
        trip.cancellationReason = "Cancelled by rider"
        return ResponseEntity.ok(toTripDto(tripRepository.save(trip)))
    }

    private fun toDto(rideRequest: RideRequest): RideRequestDto {
        return RideRequestDto(
            id = rideRequest.id.toString(),
            riderId = rideRequest.rider?.id.toString(),
            pickupLocation = toLocationDto(rideRequest.pickupLocation!!),
            dropoffLocation = toLocationDto(rideRequest.dropoffLocation!!),
            pickupAddress = rideRequest.pickupAddress,
            dropoffAddress = rideRequest.dropoffAddress,
            vehicleType = rideRequest.vehicleType,
            passengerCount = rideRequest.passengerCount,
            fareEstimate = rideRequest.fareEstimate,
            surgeMultiplier = rideRequest.surgeMultiplier,
            status = rideRequest.status,
            expiresAt = rideRequest.expiresAt,
            createdAt = rideRequest.createdAt
        )
    }

    private fun toTripDto(trip: Trip): TripDto {
        return TripDto(
            id = trip.id.toString(),
            rideRequestId = trip.rideRequest?.id.toString(),
            riderId = trip.rider?.id.toString(),
            driverId = trip.driver?.id.toString(),
            startedAt = trip.startedAt,
            endedAt = trip.endedAt,
            distanceKm = trip.distanceKm,
            durationMinutes = trip.durationMinutes,
            finalFare = trip.finalFare,
            commissionAmount = trip.commissionAmount,
            driverEarnings = trip.driverEarnings,
            status = trip.status,
            paymentMethod = trip.paymentMethod,
            createdAt = trip.createdAt
        )
    }

    private fun toLocationDto(point: Point): com.ridenorth.common.dto.LocationDto {
        return com.ridenorth.common.dto.LocationDto(
            latitude = point.y,
            longitude = point.x
        )
    }
}
