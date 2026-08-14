package com.ridenorth.module.driver

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.user.User
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "driver_profiles", indexes = [
    Index(name = "idx_driver_user", columnList = "user_id", unique = true),
    Index(name = "idx_driver_kyc", columnList = "kyc_status"),
    Index(name = "idx_driver_online", columnList = "is_online")
])
class DriverProfile(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(nullable = false, length = 30)
    var licenseNumber: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var kycStatus: KycStatus = KycStatus.PENDING,

    @Column(length = 1000)
    var verificationDocs: String? = null,

    @Column(nullable = false)
    var isOnline: Boolean = false,

    @Column(columnDefinition = "geography(Point, 4326)")
    var currentLocation: org.locationtech.jts.geom.Point? = null,

    @Column(nullable = false)
    var lastLocationUpdate: Instant? = null,

    @Column(nullable = false)
    var totalTrips: Int = 0,

    @Column(nullable = false)
    var acceptanceRate: Double = 100.0,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    override var createdAt: Instant? = null
) : BaseEntity()

enum class KycStatus {
    PENDING,
    BASIC_APPROVED,
    ENHANCED_APPROVED,
    REJECTED,
    SUSPENDED
}
