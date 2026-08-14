package com.ridenorth.module.application

import com.ridenorth.common.dto.DriverApplicationDto
import com.ridenorth.common.dto.SubmitDriverApplicationRequest
import com.ridenorth.module.driver.DriverProfile
import com.ridenorth.module.driver.DriverProfileRepository
import com.ridenorth.module.driver.KycStatus
import com.ridenorth.module.driver.Vehicle
import com.ridenorth.module.driver.VehicleRepository
import com.ridenorth.module.notification.NotificationService
import com.ridenorth.module.user.User
import com.ridenorth.module.user.UserRepository
import com.ridenorth.module.user.UserRole
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class DriverApplicationService(
    private val applicationRepository: DriverApplicationRepository,
    private val userRepository: UserRepository,
    private val driverProfileRepository: DriverProfileRepository,
    private val vehicleRepository: VehicleRepository,
    private val notificationService: NotificationService
) {

    @Transactional
    fun submit(request: SubmitDriverApplicationRequest): DriverApplicationDto {
        val existing = applicationRepository.findByPhoneNumberOrderByCreatedAtDesc(request.phoneNumber)
        if (existing.any { it.status == ApplicationStatus.PENDING }) {
            throw IllegalStateException("You already have a pending application. We will review it shortly.")
        }

        val application = DriverApplication(
            applicationRef = "RN-PENDING",
            phoneNumber = request.phoneNumber,
            fullName = request.fullName,
            ninNumber = request.ninNumber,
            licenseNumber = request.licenseNumber,
            vehicleType = request.vehicleType,
            plateNumber = request.plateNumber,
            make = request.make,
            model = request.model,
            year = request.year,
            capacity = request.capacity,
            documents = request.documents,
            status = ApplicationStatus.PENDING
        )
        val saved = applicationRepository.save(application)
        saved.applicationRef = "RN-" + saved.id.toString().take(8).uppercase(Locale.ROOT)
        return toDto(applicationRepository.save(saved))
    }

    @Transactional(readOnly = true)
    fun getLatestByPhone(phoneNumber: String): DriverApplicationDto {
        val application = applicationRepository.findByPhoneNumberOrderByCreatedAtDesc(phoneNumber)
            .firstOrNull() ?: throw IllegalArgumentException("No application found for this phone number")
        return toDto(application)
    }

    @Transactional(readOnly = true)
    fun listAll(): List<DriverApplicationDto> {
        return applicationRepository.findAllByOrderByCreatedAtDesc().map { toDto(it) }
    }

    @Transactional
    fun approve(applicationId: UUID): DriverApplicationDto {
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        if (application.status != ApplicationStatus.PENDING) {
            throw IllegalStateException("Application is already ${application.status.name.lowercase()}")
        }

        val user = userRepository.findByPhoneNumber(application.phoneNumber).orElseGet {
            userRepository.save(
                User(
                    phoneNumber = application.phoneNumber,
                    name = application.fullName,
                    role = UserRole.DRIVER,
                    ninNumber = application.ninNumber,
                    isActive = true
                )
            )
        }
        if (user.ninNumber == null) user.ninNumber = application.ninNumber
        if (user.name.isBlank()) user.name = application.fullName
        if (user.role != UserRole.DRIVER) user.role = UserRole.DRIVER

        val profile = driverProfileRepository.findByUserId(user.id!!).orElseGet {
            driverProfileRepository.save(
                DriverProfile(
                    user = user,
                    licenseNumber = application.licenseNumber,
                    kycStatus = KycStatus.BASIC_APPROVED,
                    verificationDocs = application.documents
                )
            )
        }
        profile.licenseNumber = application.licenseNumber
        profile.kycStatus = KycStatus.BASIC_APPROVED

        vehicleRepository.save(
            Vehicle(
                owner = user,
                type = application.vehicleType,
                plateNumber = application.plateNumber,
                capacity = application.capacity,
                isVerified = true,
                photos = application.documents,
                make = application.make,
                model = application.model,
                year = application.year
            )
        )

        application.status = ApplicationStatus.APPROVED
        application.reviewedAt = Instant.now()
        val updated = applicationRepository.save(application)

        notificationService.sendDriverApprovalSms(application.phoneNumber, application.fullName)
        return toDto(updated)
    }

    @Transactional
    fun reject(applicationId: UUID, reason: String): DriverApplicationDto {
        val application = applicationRepository.findById(applicationId)
            .orElseThrow { IllegalArgumentException("Application not found") }
        if (application.status != ApplicationStatus.PENDING) {
            throw IllegalStateException("Application is already ${application.status.name.lowercase()}")
        }

        application.status = ApplicationStatus.REJECTED
        application.rejectionReason = reason
        application.reviewedAt = Instant.now()
        val updated = applicationRepository.save(application)

        notificationService.sendDriverRejectionSms(application.phoneNumber, reason)
        return toDto(updated)
    }

    private fun toDto(app: DriverApplication): DriverApplicationDto {
        return DriverApplicationDto(
            id = app.id.toString(),
            applicationRef = app.applicationRef,
            phoneNumber = app.phoneNumber,
            fullName = app.fullName,
            ninNumber = app.ninNumber,
            licenseNumber = app.licenseNumber,
            vehicleType = app.vehicleType,
            plateNumber = app.plateNumber,
            make = app.make,
            model = app.model,
            year = app.year,
            capacity = app.capacity,
            documents = app.documents,
            status = app.status,
            rejectionReason = app.rejectionReason,
            submittedAt = app.createdAt,
            reviewedAt = app.reviewedAt
        )
    }
}
