package com.ridenorth.module.user

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.user.User
import com.ridenorth.module.booking.Trip
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "sos_events", indexes = [
    Index(name = "idx_sos_user", columnList = "user_id"),
    Index(name = "idx_sos_trip", columnList = "trip_id"),
    Index(name = "idx_sos_status", columnList = "status")
])
class SosEvent(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    var trip: Trip? = null,

    @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
    var location: org.locationtech.jts.geom.Point? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: SosStatus = SosStatus.ACTIVE,

    @Column(length = 500)
    var notes: String? = null,

    @Column
    var resolvedAt: Instant? = null,

    @Column(nullable = false)
    var createdAt: Instant? = null
) : BaseEntity()

enum class SosStatus {
    ACTIVE,
    RESPONDED,
    RESOLVED,
    CANCELLED
}
