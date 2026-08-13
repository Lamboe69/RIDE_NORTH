package com.ridenorth.module.scheduled

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScheduledRouteRepository : JpaRepository<ScheduledRoute, UUID> {
    fun findByOperatorId(operatorId: UUID): List<ScheduledRoute>
    fun findByStatus(status: RouteStatus): List<ScheduledRoute>
    fun findByOriginAndDestination(origin: String, destination: String): List<ScheduledRoute>

    @Query("""
        SELECT r FROM ScheduledRoute r
        WHERE r.origin = :origin AND r.destination = :destination
        AND r.departureTime >= :fromDate
        AND r.status = 'SCHEDULED'
    """)
    fun findUpcomingRoutes(
        @Param("origin") origin: String,
        @Param("destination") destination: String,
        @Param("fromDate") fromDate: java.time.Instant
    ): List<ScheduledRoute>
}
