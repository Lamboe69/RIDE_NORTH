package com.ridenorth.module.location

import com.ridenorth.common.dto.LocationDto
import com.ridenorth.module.driver.DriverProfileRepository
import com.ridenorth.module.user.UserRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/location")
class LocationController(
    private val locationService: LocationService,
    private val userRepository: UserRepository,
    private val driverProfileRepository: DriverProfileRepository
) {

    @PostMapping("/update")
    @PreAuthorize("hasRole('DRIVER')")
    fun updateLocation(@Valid @RequestBody request: LocationDto): ResponseEntity<Map<String, String>> {
        val phoneNumber = org.springframework.security.core.context.SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val profile = driverProfileRepository.findByUserId(user.id!!)
            .orElseThrow { IllegalArgumentException("Driver profile not found") }

        locationService.updateDriverLocation(profile.id!!, request.latitude, request.longitude)
        return ResponseEntity.ok(mapOf("status" to "updated"))
    }
}
