package com.ridenorth.module.booking

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RideRequestRepository : JpaRepository<RideRequest, UUID> {
    fun findByRiderId(riderId: UUID): List<RideRequest>
    fun findByStatus(status: RideRequestStatus): List<RideRequest>
    fun findByStatusInAndExpiresAtBefore(statuses: List<RideRequestStatus>, expiresAt: java.time.Instant): List<RideRequest>
}
