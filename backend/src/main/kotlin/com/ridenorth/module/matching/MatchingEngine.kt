package com.ridenorth.module.matching

import com.ridenorth.common.dto.AcceptTripRequest
import com.ridenorth.module.booking.RideRequest
import com.ridenorth.module.booking.RideRequestRepository
import com.ridenorth.module.booking.RideRequestStatus
import com.ridenorth.module.driver.DriverProfile
import com.ridenorth.module.driver.DriverProfileRepository
import com.ridenorth.module.notification.NotificationService
import org.locationtech.jts.geom.Point
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MatchingEngine(
    private val driverProfileRepository: DriverProfileRepository,
    private val rideRequestRepository: RideRequestRepository,
    private val notificationService: NotificationService
) {

    @Transactional
    fun matchDriver(rideRequest: RideRequest) {
        val pickupLocation = rideRequest.pickupLocation ?: return
        val vehicleType = rideRequest.vehicleType
        val initialRadiusMeters = 2000.0
        val maxRadiusMeters = 10000.0
        val radiusStep = 3000.0

        var currentRadius = initialRadiusMeters
        var matchedDriver: DriverProfile? = null

        while (matchedDriver == null && currentRadius <= maxRadiusMeters) {
            val nearbyDrivers = driverProfileRepository.findNearbyOnlineDrivers(pickupLocation, currentRadius)
            matchedDriver = nearbyDrivers
                .filter { it.isActive && it.kycStatus != com.ridenorth.module.driver.KycStatus.SUSPENDED }
                .minByOrNull { it.acceptanceRate }
            if (matchedDriver != null) break
            currentRadius += radiusStep
        }

        if (matchedDriver != null) {
            rideRequest.status = RideRequestStatus.ACCEPTED
            rideRequestRepository.save(rideRequest)
            notificationService.notifyDriverOfRideRequest(matchedDriver, rideRequest)
        } else {
            rideRequest.status = RideRequestStatus.EXPIRED
            rideRequestRepository.save(rideRequest)
            notificationService.notifyRiderNoDriversFound(rideRequest)
        }
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    fun expireOldRequests() {
        val now = java.time.Instant.now()
        val expiredRequests = rideRequestRepository.findByStatusInAndExpiresAtBefore(
            listOf(RideRequestStatus.SEARCHING, RideRequestStatus.ACCEPTED),
            now
        )
        expiredRequests.forEach { request ->
            request.status = RideRequestStatus.EXPIRED
            rideRequestRepository.save(request)
        }
    }
}
