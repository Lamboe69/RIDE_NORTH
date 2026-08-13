package com.ridenorth.module.payment

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.user.User
import com.ridenorth.module.booking.Trip
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "payments", indexes = [
    Index(name = "idx_payment_payer", columnList = "payer_id"),
    Index(name = "idx_payment_payee", columnList = "payee_id"),
    Index(name = "idx_payment_status", columnList = "status"),
    Index(name = "idx_payment_method", columnList = "payment_method")
])
class Payment(
    @Column(name = "payer_id", nullable = false)
    var payerId: java.util.UUID? = null,

    @Column(name = "payee_id", nullable = false)
    var payeeId: java.util.UUID? = null,

    @Column(name = "trip_id")
    var tripId: java.util.UUID? = null,

    @Column(name = "freight_job_id")
    var freightJobId: java.util.UUID? = null,

    @Column(name = "scheduled_route_id")
    var scheduledRouteId: java.util.UUID? = null,

    @Column(nullable = false)
    var amount: Double = 0.0,

    @Column(nullable = false)
    var commissionAmount: Double = 0.0,

    @Column(nullable = false)
    var netAmount: Double = 0.0,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    var paymentMethod: PaymentMethod = PaymentMethod.CASH,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: PaymentStatus = PaymentStatus.PENDING,

    @Column(length = 100)
    var providerTransactionId: String? = null,

    @Column(length = 500)
    var failureReason: String? = null,

    @Column(nullable = false)
    var createdAt: Instant? = null
) : BaseEntity()

enum class PaymentMethod {
    CASH,
    MTN_MOMO,
    AIRTEL_MONEY,
    BANK_TRANSFER
}

enum class PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    REFUNDED,
    ESCROW_HELD,
    ESCROW_RELEASED
}
