package com.ridenorth.module.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentRepository : JpaRepository<Payment, UUID> {
    fun findByPayerId(payerId: UUID): List<Payment>
    fun findByPayeeId(payeeId: UUID): List<Payment>
    fun findByTripId(tripId: UUID): Optional<Payment>
    fun findByFreightJobId(freightJobId: UUID): Optional<Payment>
    fun findByStatus(status: PaymentStatus): List<Payment>

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p
        WHERE p.payeeId = :payeeId AND p.status = 'SUCCESS'
    """)
    fun sumSuccessfulPaymentsToPayee(@Param("payeeId") payeeId: UUID): Double

    @Query("""
        SELECT COALESCE(SUM(p.commissionAmount), 0.0) FROM Payment p
        WHERE p.status = 'SUCCESS'
    """)
    fun sumTotalCommissions(): Double
}
