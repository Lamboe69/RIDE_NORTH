package com.ridenorth.module.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SosEventRepository : JpaRepository<SosEvent, UUID> {
    fun findByUserId(userId: UUID): List<SosEvent>
    fun findByStatus(status: SosStatus): List<SosEvent>
    fun findByTripId(tripId: UUID): Optional<SosEvent>
}
