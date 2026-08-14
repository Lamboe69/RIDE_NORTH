package com.ridenorth.module.application

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.driver.VehicleType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "driver_applications", indexes = [
    Index(name = "idx_app_phone", columnList = "phone_number"),
    Index(name = "idx_app_status", columnList = "status"),
    Index(name = "idx_app_ref", columnList = "application_ref", unique = true)
])
class DriverApplication(
    @Column(nullable = false, unique = true, length = 30)
    var applicationRef: String = "",

    @Column(nullable = false, length = 20)
    var phoneNumber: String = "",

    @Column(nullable = false, length = 100)
    var fullName: String = "",

    @Column(length = 20)
    var ninNumber: String? = null,

    @Column(nullable = false, length = 30)
    var licenseNumber: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var vehicleType: VehicleType = VehicleType.BODA,

    @Column(nullable = false, length = 20)
    var plateNumber: String = "",

    @Column(length = 100)
    var make: String? = null,

    @Column(length = 100)
    var model: String? = null,

    @Column(length = 20)
    var year: String? = null,

    @Column(nullable = false)
    var capacity: Int = 1,

    @Column(length = 1000)
    var documents: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ApplicationStatus = ApplicationStatus.PENDING,

    @Column(length = 500)
    var rejectionReason: String? = null,

    @Column
    var reviewedAt: Instant? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    override var createdAt: Instant? = null
) : BaseEntity()

enum class ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED
}
