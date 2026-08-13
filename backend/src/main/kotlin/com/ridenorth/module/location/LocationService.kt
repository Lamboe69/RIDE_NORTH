package com.ridenorth.module.location

import com.ridenorth.module.driver.DriverProfileRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class LocationService(
    private val driverProfileRepository: DriverProfileRepository,
    private val messagingTemplate: SimpMessagingTemplate
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    @Transactional
    fun updateDriverLocation(driverId: UUID, latitude: Double, longitude: Double): DriverProfile {
        val profile = driverProfileRepository.findById(driverId)
            .orElseThrow { IllegalArgumentException("Driver not found") }

        val point: Point = geometryFactory.createPoint(Coordinate(longitude, latitude))
        profile.currentLocation = point
        profile.lastLocationUpdate = java.time.Instant.now()
        val saved = driverProfileRepository.save(profile)

        messagingTemplate.convertAndSend(
            "/topic/driver-location/$driverId",
            mapOf(
                "driverId" to driverId.toString(),
                "latitude" to latitude,
                "longitude" to longitude,
                "timestamp" to java.time.Instant.now().toString()
            )
        )

        return saved
    }
}
