package com.ridenorth.module.driver

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "vehicles", indexes = [
    Index(name = "idx_vehicles_type", columnList = "type"),
    Index(name = "idx_vehicles_owner", columnList = "owner_id")
])
class Vehicle(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var type: VehicleType = VehicleType.BODA,

    @Column(nullable = false, length = 20)
    var plateNumber: String = "",

    @Column(nullable = false)
    var capacity: Int = 1,

    @Column(nullable = false)
    var isVerified: Boolean = false,

    @Column(length = 500)
    var photos: String? = null,

    @Column(length = 100)
    var make: String? = null,

    @Column(length = 100)
    var model: String? = null,

    @Column(length = 20)
    var year: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false)
    var createdAt: Instant? = null
) : BaseEntity()

enum class VehicleType(val displayName: String, val passengerCapacity: Int) {
    BODA("Boda Boda", 1),
    BICYCLE_BODA("Bicycle Boda", 1),
    TUKTUK("Tuk Tuk", 3),
    CAR("Private Car", 4),
    MINIBUS("Minibus", 14),
    COACH("Long-distance Coach", 40),
    PICKUP("Pickup Truck", 2),
    TRUCK("Truck (Medium)", 3),
    LORRY("Lorry (Heavy)", 5),
    TRACTOR("Tractor & Trailer", 2),
    MOTORCYCLE_COURIER("Boda Courier", 1),
    AMBULANCE("Ambulance", 2),
    FERRY("Ferry / Boat", 20)
}
