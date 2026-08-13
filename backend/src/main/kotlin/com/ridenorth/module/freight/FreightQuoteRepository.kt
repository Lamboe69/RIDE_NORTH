package com.ridenorth.module.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FreightQuoteRepository : JpaRepository<FreightQuote, UUID> {
    fun findByFreightJobId(freightJobId: UUID): List<FreightQuote>
    fun findByDriverId(driverId: UUID): List<FreightQuote>
    fun findByFreightJobIdAndStatus(freightJobId: UUID, status: QuoteStatus): List<FreightQuote>
}
