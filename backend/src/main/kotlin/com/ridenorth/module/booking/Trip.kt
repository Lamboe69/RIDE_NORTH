package com.ridenorth.module.booking

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.driver.DriverProfile
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "trips", indexes = [
    Index(name = "idx_trip_rider", columnList = "rider_id"),
    Index(name = "idx_trip_driver", columnList = "driver_id"),
    Index(name = "idx_trip_status", columnList = "status"),
    Index(name = "idx_trip_request", columnList = "ride_request_id", unique = true)
])
class Trip(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_request_id", nullable = false)
    var rideRequest: RideRequest? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    var rider: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    var driver: DriverProfile? = null,

    @Column(nullable = false)
    var startedAt: Instant? = null,

    @Column(nullable = false)
    var endedAt: Instant? = null,

    @Column(nullable = false)
    var distanceKm: Double = 0.0,

    @Column(nullable = false)
    var durationMinutes: Int = 0,

    @Column(nullable = false)
    var finalFare: Double = 0.0,

    @Column(nullable = false)
    var commissionAmount: Double = 0.0,

    @Column(nullable = false)
    var driverEarnings: Double = 0.0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: TripStatus = TripStatus.STARTED,

    @Column(length = 500)
    var cancellationReason: String? = null,

    @Column(nullable = false)
    var paymentMethod: String = "CASH",

    @Column(nullable = false)
    override var createdAt: Instant? = null
) : BaseEntity()

enum class TripStatus {
    STARTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    DISPUTED
}
