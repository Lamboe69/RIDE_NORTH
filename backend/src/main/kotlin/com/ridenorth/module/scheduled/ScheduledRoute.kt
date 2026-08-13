package com.ridenorth.module.scheduled

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "scheduled_routes", indexes = [
    Index(name = "idx_route_operator", columnList = "operator_id"),
    Index(name = "idx_route_status", columnList = "status")
])
class ScheduledRoute(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    var operator: User? = null,

    @Column(nullable = false, length = 100)
    var origin: String = "",

    @Column(nullable = false, length = 100)
    var destination: String = "",

    @Column(columnDefinition = "geography(Point, 4326)")
    var originLocation: org.locationtech.jts.geom.Point? = null,

    @Column(columnDefinition = "geography(Point, 4326)")
    var destinationLocation: org.locationtech.jts.geom.Point? = null,

    @Column(nullable = false)
    var departureTime: Instant? = null,

    @Column(nullable = false)
    var seatCapacity: Int = 0,

    @Column(nullable = false)
    var seatsBooked: Int = 0,

    @Column(nullable = false)
    var pricePerSeat: Double = 0.0,

    @Column(nullable = false)
    var minSeatsToConfirm: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: RouteStatus = RouteStatus.SCHEDULED,

    @Column(length = 500)
    var notes: String? = null,

    @Column(nullable = false)
    var createdAt: Instant? = null
) : BaseEntity()

enum class RouteStatus {
    SCHEDULED,
    CONFIRMED,
    DEPARTED,
    COMPLETED,
    CANCELLED
}
