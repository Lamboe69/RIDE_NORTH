package com.ridenorth.module.booking

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TripRepository : JpaRepository<Trip, UUID> {
    fun findByRiderId(riderId: UUID): List<Trip>
    fun findByDriverId(driverId: UUID): List<Trip>
    fun findByRideRequestId(rideRequestId: UUID): Optional<Trip>
    fun findByStatus(status: TripStatus): List<Trip>

    @Query("""
        SELECT COUNT(t) FROM Trip t
        WHERE t.driver.id = :driverId AND t.status = 'COMPLETED'
    """)
    fun countCompletedByDriver(@Param("driverId") driverId: UUID): Long
}
