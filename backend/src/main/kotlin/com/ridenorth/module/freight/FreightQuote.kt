package com.ridenorth.module.freight

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.driver.DriverProfile
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "freight_quotes", indexes = [
    Index(name = "idx_quote_job", columnList = "freight_job_id"),
    Index(name = "idx_quote_driver", columnList = "driver_id"),
    Index(name = "idx_quote_status", columnList = "status")
])
class FreightQuote(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freight_job_id", nullable = false)
    var freightJob: FreightJob? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    var driver: DriverProfile? = null,

    @Column(nullable = false)
    var quotedPrice: Double = 0.0,

    @Column(nullable = false)
    var estimatedDurationHours: Double = 0.0,

    @Column(length = 500)
    var message: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: QuoteStatus = QuoteStatus.PENDING,

    @Column(nullable = false)
    override var createdAt: Instant? = null
) : BaseEntity()

enum class QuoteStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    EXPIRED
}
