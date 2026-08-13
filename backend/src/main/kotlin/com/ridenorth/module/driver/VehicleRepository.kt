package com.ridenorth.module.driver

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface VehicleRepository : JpaRepository<Vehicle, UUID> {
    fun findByOwnerId(ownerId: UUID): List<Vehicle>
    fun findByType(type: VehicleType): List<Vehicle>
    fun findByIsVerifiedAndIsActiveTrue(isVerified: Boolean): List<Vehicle>
}
