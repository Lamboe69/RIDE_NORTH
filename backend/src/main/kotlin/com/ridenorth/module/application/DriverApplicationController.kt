package com.ridenorth.module.application

import com.ridenorth.common.dto.DriverApplicationDto
import com.ridenorth.common.dto.ReviewApplicationRequest
import com.ridenorth.common.dto.SubmitDriverApplicationRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class DriverApplicationController(private val applicationService: DriverApplicationService) {

    @PostMapping("/api/public/driver-application")
    fun submit(
        @Valid @RequestBody request: SubmitDriverApplicationRequest
    ): ResponseEntity<DriverApplicationDto> {
        return ResponseEntity.ok(applicationService.submit(request))
    }

    @GetMapping("/api/public/driver-application/status")
    fun status(
        @RequestParam phoneNumber: String
    ): ResponseEntity<DriverApplicationDto> {
        return ResponseEntity.ok(applicationService.getLatestByPhone(phoneNumber))
    }

    @GetMapping("/api/admin/driver-applications")
    @PreAuthorize("hasRole('ADMIN')")
    fun listAll(): ResponseEntity<List<DriverApplicationDto>> {
        return ResponseEntity.ok(applicationService.listAll())
    }

    @PostMapping("/api/admin/driver-applications/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    fun approve(@PathVariable id: UUID): ResponseEntity<DriverApplicationDto> {
        return ResponseEntity.ok(applicationService.approve(id))
    }

    @PostMapping("/api/admin/driver-applications/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    fun reject(
        @PathVariable id: UUID,
        @Valid @RequestBody(required = false) request: ReviewApplicationRequest
    ): ResponseEntity<DriverApplicationDto> {
        val reason = request?.rejectionReason?.takeIf { it.isNotBlank() }
            ?: "Your documents did not meet our verification requirements"
        return ResponseEntity.ok(applicationService.reject(id, reason))
    }
}
