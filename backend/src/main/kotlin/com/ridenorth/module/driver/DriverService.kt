package com.ridenorth.module.driver

import com.ridenorth.module.user.UserRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DriverService(
    private val userRepository: UserRepository,
    private val driverProfileRepository: DriverProfileRepository,
    private val vehicleRepository: VehicleRepository
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    @Transactional
    fun updateLocation(latitude: Double, longitude: Double): DriverProfile {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val profile = driverProfileRepository.findByUserId(user.id!!)
            .orElseThrow { IllegalArgumentException("Driver profile not found") }

        val point: Point = geometryFactory.createPoint(Coordinate(longitude, latitude))
        profile.currentLocation = point
        profile.lastLocationUpdate = java.time.Instant.now()
        return driverProfileRepository.save(profile)
    }

    @Transactional
    fun updateOnlineStatus(isOnline: Boolean): DriverProfile {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val profile = driverProfileRepository.findByUserId(user.id!!)
            .orElseThrow { IllegalArgumentException("Driver profile not found") }

        profile.isOnline = isOnline
        return driverProfileRepository.save(profile)
    }

    @Transactional(readOnly = true)
    fun getMyProfile(): DriverProfile {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        return driverProfileRepository.findByUserId(user.id!!)
            .orElseThrow { IllegalArgumentException("Driver profile not found") }
    }
}
