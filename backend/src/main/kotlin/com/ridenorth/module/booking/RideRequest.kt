package com.ridenorth.module.booking

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "ride_requests", indexes = [
    Index(name = "idx_ride_rider", columnList = "rider_id"),
    Index(name = "idx_ride_status", columnList = "status"),
    Index(name = "idx_ride_pickup", columnList = "pickup_location", postgresqlExclude = " USING gist (pickup_location)")
])
class RideRequest(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    var rider: User? = null,

    @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
    var pickupLocation: org.locationtech.jts.geom.Point? = null,

    @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
    var dropoffLocation: org.locationtech.jts.geom.Point? = null,

    @Column(nullable = false, length = 200)
    var pickupAddress: String = "",

    @Column(nullable = false, length = 200)
    var dropoffAddress: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var vehicleType: VehicleType = VehicleType.BODA,

    @Column(nullable = false)
    var passengerCount: Int = 1,

    @Column(nullable = false)
    var fareEstimate: Double = 0.0,

    @Column(nullable = false)
    var surgeMultiplier: Double = 1.0,

    @Column(length = 500)
    var notes: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: RideRequestStatus = RideRequestStatus.PENDING,

    @Column(nullable = false)
    var expiresAt: Instant? = null,

    @Column(nullable = false)
    var createdAt: Instant? = null
) : BaseEntity()

enum class RideRequestStatus {
    PENDING,
    SEARCHING,
    ACCEPTED,
    ARRIVED_AT_PICKUP,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    EXPIRED
}
