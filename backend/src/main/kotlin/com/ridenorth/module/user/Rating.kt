package com.ridenorth.module.user

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.booking.Trip
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "ratings", indexes = [
    Index(name = "idx_rating_trip", columnList = "trip_id", unique = true),
    Index(name = "idx_rating_rater", columnList = "rater_id"),
    Index(name = "idx_rating_ratee", columnList = "ratee_id")
])
class Rating(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    var trip: Trip? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rater_id", nullable = false)
    var rater: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratee_id", nullable = false)
    var ratee: User? = null,

    @Column(nullable = false)
    var score: Int = 5,

    @Column(length = 500)
    var comment: String? = null,

    @Column(nullable = false)
    override var createdAt: Instant? = null
) : BaseEntity()
