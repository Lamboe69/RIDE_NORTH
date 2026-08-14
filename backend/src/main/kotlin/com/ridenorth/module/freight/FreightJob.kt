package com.ridenorth.module.freight

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.driver.VehicleType
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "freight_jobs", indexes = [
    Index(name = "idx_freight_shipper", columnList = "shipper_id"),
    Index(name = "idx_freight_status", columnList = "status"),
    Index(name = "idx_freight_pickup", columnList = "pickup_location")
])
class FreightJob(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id", nullable = false)
    var shipper: User? = null,

    @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
    var pickupLocation: org.locationtech.jts.geom.Point? = null,

    @Column(columnDefinition = "geography(Point, 4326)", nullable = false)
    var dropoffLocation: org.locationtech.jts.geom.Point? = null,

    @Column(nullable = false, length = 200)
    var pickupAddress: String = "",

    @Column(nullable = false, length = 200)
    var dropoffAddress: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var cargoType: CargoType = CargoType.GENERAL,

    @Column(nullable = false)
    var estimatedWeightKg: Double = 0.0,

    @Column(nullable = false)
    var estimatedVolumeM3: Double = 0.0,

    @Column(length = 500)
    var cargoDescription: String? = null,

    @Column
    var preferredDate: Instant? = null,

    @Column
    var preferredVehicleType: VehicleType? = null,

    @Column(nullable = false)
    var minPrice: Double = 0.0,

    @Column(nullable = false)
    var maxPrice: Double = 0.0,

    @Column(length = 500)
    var specialInstructions: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: FreightJobStatus = FreightJobStatus.OPEN,

    @Column(nullable = false)
    override var createdAt: Instant? = null
) : BaseEntity()

enum class CargoType {
    AGRICULTURAL,
    CONSTRUCTION,
    GENERAL,
    PERISHABLE,
    HAZARDOUS,
    FURNITURE,
    OTHER
}

enum class FreightJobStatus {
    OPEN,
    QUOTED,
    ASSIGNED,
    PICKED_UP,
    IN_TRANSIT,
    DELIVERED,
    COMPLETED,
    CANCELLED
}
