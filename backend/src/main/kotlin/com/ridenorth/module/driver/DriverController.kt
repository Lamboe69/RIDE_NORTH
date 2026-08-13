package com.ridenorth.module.driver

import com.ridenorth.common.dto.*
import com.ridenorth.module.driver.DriverProfileRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/driver")
class DriverController(private val driverService: DriverService, private val driverProfileRepository: DriverProfileRepository) {

    @PostMapping("/location")
    @PreAuthorize("hasRole('DRIVER')")
    fun updateLocation(@Valid @RequestBody request: UpdateDriverLocationDto): ResponseEntity<DriverProfileDto> {
        val profile = driverService.updateLocation(request.latitude, request.longitude)
        return ResponseEntity.ok(toDto(profile))
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('DRIVER')")
    fun updateOnlineStatus(@Valid @RequestBody request: UpdateOnlineStatusDto): ResponseEntity<DriverProfileDto> {
        val profile = driverService.updateOnlineStatus(request.isOnline)
        return ResponseEntity.ok(toDto(profile))
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    fun getMyProfile(): ResponseEntity<DriverProfileDto> {
        val profile = driverService.getMyProfile()
        return ResponseEntity.ok(toDto(profile))
    }

    @GetMapping("/online")
    @PreAuthorize("hasRole('RIDER')")
    fun getOnlineDrivers(): ResponseEntity<List<DriverProfileDto>> {
        val profiles = driverProfileRepository.findByIsOnlineTrue()
        return ResponseEntity.ok(profiles.map { toDto(it) })
    }

    private fun toDto(profile: DriverProfile): DriverProfileDto {
        return DriverProfileDto(
            userId = profile.user?.id.toString(),
            licenseNumber = profile.licenseNumber,
            kycStatus = profile.kycStatus.name,
            isOnline = profile.isOnline,
            currentLocation = profile.currentLocation?.let { toLocationDto(it) },
            totalTrips = profile.totalTrips,
            acceptanceRate = profile.acceptanceRate
        )
    }

    private fun toLocationDto(point: org.locationtech.jts.geom.Point): com.ridenorth.common.dto.LocationDto {
        return com.ridenorth.common.dto.LocationDto(
            latitude = point.y,
            longitude = point.x
        )
    }
}
