package com.ridenorth.module.driver

import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DriverProfileRepository : JpaRepository<DriverProfile, UUID> {
    fun findByUserId(userId: UUID): Optional<DriverProfile>
    fun findByIsOnlineTrue(): List<DriverProfile>
    fun findByKycStatus(kycStatus: KycStatus): List<DriverProfile>

    @Query("""
        SELECT d FROM DriverProfile d
        WHERE d.isOnline = true
        AND d.kycStatus IN ('BASIC_APPROVED', 'ENHANCED_APPROVED')
        AND d.currentLocation IS NOT NULL
        AND ST_DWithin(d.currentLocation, :location, :radiusMeters) = true
        AND d.isActive = true
    """)
    fun findNearbyOnlineDrivers(
        @Param("location") location: Point,
        @Param("radiusMeters") radiusMeters: Double
    ): List<DriverProfile>

    @Modifying
    @Query("UPDATE DriverProfile d SET d.currentLocation = :location, d.lastLocationUpdate = CURRENT_TIMESTAMP WHERE d.user.id = :userId")
    fun updateLocation(@Param("userId") userId: UUID, @Param("location") location: Point)

    @Modifying
    @Query("UPDATE DriverProfile d SET d.isOnline = :isOnline WHERE d.user.id = :userId")
    fun updateOnlineStatus(@Param("userId") userId: UUID, @Param("isOnline") isOnline: Boolean)
}
