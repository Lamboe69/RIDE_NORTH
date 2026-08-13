package com.ridenorth.module.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FreightJobRepository : JpaRepository<FreightJob, UUID> {
    fun findByShipperId(shipperId: UUID): List<FreightJob>
    fun findByStatus(status: FreightJobStatus): List<FreightJob>
    fun findByPreferredVehicleType(vehicleType: com.ridenorth.module.driver.VehicleType): List<FreightJob>
}
