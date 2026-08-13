package com.ridenorth.module.booking

import com.ridenorth.common.dto.*
import com.ridenorth.module.driver.DriverProfile
import com.ridenorth.module.driver.DriverProfileRepository
import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.matching.MatchingEngine
import com.ridenorth.module.payment.PaymentService
import com.ridenorth.module.pricing.PricingService
import com.ridenorth.module.user.RatingRepository
import com.ridenorth.module.user.UserRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class BookingService(
    private val rideRequestRepository: RideRequestRepository,
    private val tripRepository: TripRepository,
    private val driverProfileRepository: DriverProfileRepository,
    private val userRepository: UserRepository,
    private val ratingRepository: RatingRepository,
    private val matchingEngine: MatchingEngine,
    private val pricingService: PricingService,
    private val paymentService: PaymentService
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    @Transactional
    fun createRideRequest(request: CreateRideRequestDto): RideRequest {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val rider = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val pickupPoint: Point = geometryFactory.createPoint(Coordinate(request.pickupLongitude, request.pickupLatitude))
        val dropoffPoint: Point = geometryFactory.createPoint(Coordinate(request.dropoffLongitude, request.dropoffLatitude))

        val fareEstimate = pricingService.calculateFareEstimate(
            pickupPoint, dropoffPoint, request.vehicleType, request.passengerCount
        )

        val rideRequest = RideRequest(
            rider = rider,
            pickupLocation = pickupPoint,
            dropoffLocation = dropoffPoint,
            pickupAddress = request.pickupAddress,
            dropoffAddress = request.dropoffAddress,
            vehicleType = request.vehicleType,
            passengerCount = request.passengerCount,
            fareEstimate = fareEstimate,
            status = com.ridenorth.module.booking.RideRequestStatus.SEARCHING,
            expiresAt = Instant.now().plusSeconds(120),
            createdAt = Instant.now()
        )

        val saved = rideRequestRepository.save(rideRequest)
        matchingEngine.matchDriver(saved)
        return saved
    }

    @Transactional
    fun acceptRideRequest(rideRequestId: UUID): Trip {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val driverProfile = driverProfileRepository.findByUserId(user.id!!)
            .orElseThrow { IllegalArgumentException("Driver profile not found") }

        val rideRequest = rideRequestRepository.findById(rideRequestId)
            .orElseThrow { IllegalArgumentException("Ride request not found") }

        if (rideRequest.status != com.ridenorth.module.booking.RideRequestStatus.SEARCHING) {
            throw IllegalStateException("Ride request is no longer available")
        }

        rideRequest.status = com.ridenorth.module.booking.RideRequestStatus.ACCEPTED
        rideRequestRepository.save(rideRequest)

        val trip = Trip(
            rideRequest = rideRequest,
            rider = rideRequest.rider,
            driver = driverProfile,
            status = com.ridenorth.module.booking.TripStatus.STARTED,
            finalFare = rideRequest.fareEstimate,
            commissionAmount = rideRequest.fareEstimate * 0.15,
            driverEarnings = rideRequest.fareEstimate * 0.85,
            paymentMethod = "CASH",
            startedAt = Instant.now(),
            createdAt = Instant.now()
        )

        val savedTrip = tripRepository.save(trip)
        driverProfile.totalTrips++
        driverProfileRepository.save(driverProfile)

        return savedTrip
    }

    @Transactional
    fun startTrip(tripId: UUID): Trip {
        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found") }
        trip.status = com.ridenorth.module.booking.TripStatus.IN_PROGRESS
        trip.startedAt = Instant.now()
        return tripRepository.save(trip)
    }

    @Transactional
    fun completeTrip(tripId: UUID): Trip {
        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found") }

        val now = Instant.now()
        trip.endedAt = now
        trip.status = com.ridenorth.module.booking.TripStatus.COMPLETED

        val durationMinutes = java.time.Duration.between(trip.startedAt, now).toMinutes().coerceAtLeast(1)
        trip.durationMinutes = durationMinutes.toInt()

        tripRepository.save(trip)

        val rider = trip.rider
        val driver = trip.driver

        paymentService.createPaymentForTrip(trip)

        updateRating(rider!!.id!!, trip)
        updateRating(driver!!.user!!.id!!, trip)

        return trip
    }

    @Transactional(readOnly = true)
    fun getMyTrips(): List<Trip> {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        return when (user.role) {
            UserRole.RIDER -> tripRepository.findByRiderId(user.id!!)
            UserRole.DRIVER -> {
                val profile = driverProfileRepository.findByUserId(user.id!!)
                    .orElseThrow { IllegalArgumentException("Driver profile not found") }
                tripRepository.findByDriverId(profile.id!!)
            }
            else -> emptyList()
        }
    }

    @Transactional(readOnly = true)
    fun getTripById(tripId: UUID): Trip {
        return tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found") }
    }

    private fun updateRating(userId: UUID, trip: Trip) {
        val existing = ratingRepository.findByTripId(trip.id!!)
        if (existing.isEmpty) {
            val avgScore = ratingRepository.averageScoreForRatee(userId) ?: 5.0
            // Rating is created by the other party; we just update the average here
        }
    }
}
