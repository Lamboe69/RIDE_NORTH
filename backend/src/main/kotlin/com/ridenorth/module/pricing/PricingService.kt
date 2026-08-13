package com.ridenorth.module.pricing

import com.ridenorth.module.driver.VehicleType
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Service
import java.util.*

@Service
class PricingService {

    private val baseFares = mapOf(
        VehicleType.BODA to 1000.0,
        VehicleType.BICYCLE_BODA to 500.0,
        VehicleType.TUKTUK to 1500.0,
        VehicleType.CAR to 3000.0,
        VehicleType.MOTORCYCLE_COURIER to 1200.0,
        VehicleType.PICKUP to 5000.0,
        VehicleType.TRUCK to 15000.0,
        VehicleType.LORRY to 30000.0,
        VehicleType.TRACTOR to 20000.0,
        VehicleType.MINIBUS to 2000.0,
        VehicleType.COACH to 15000.0
    )

    private val perKmRates = mapOf(
        VehicleType.BODA to 800.0,
        VehicleType.BICYCLE_BODA to 400.0,
        VehicleType.TUKTUK to 1000.0,
        VehicleType.CAR to 1500.0,
        VehicleType.MOTORCYCLE_COURIER to 900.0,
        VehicleType.PICKUP to 2000.0,
        VehicleType.TRUCK to 5000.0,
        VehicleType.LORRY to 8000.0,
        VehicleType.TRACTOR to 6000.0,
        VehicleType.MINIBUS to 500.0,
        VehicleType.COACH to 2000.0
    )

    private val perMinuteRates = mapOf(
        VehicleType.BODA to 50.0,
        VehicleType.BICYCLE_BODA to 30.0,
        VehicleType.TUKTUK to 70.0,
        VehicleType.CAR to 100.0,
        VehicleType.MOTORCYCLE_COURIER to 60.0,
        VehicleType.PICKUP to 200.0,
        VehicleType.TRUCK to 500.0,
        VehicleType.LORRY to 800.0,
        VehicleType.TRACTOR to 600.0,
        VehicleType.MINIBUS to 20.0,
        VehicleType.COACH to 300.0
    )

    fun calculateFareEstimate(pickup: Point, dropoff: Point, vehicleType: VehicleType, passengerCount: Int): Double {
        val distanceKm = calculateDistanceKm(pickup, dropoff)
        val estimatedMinutes = estimateDurationMinutes(distanceKm)

        val baseFare = baseFares[vehicleType] ?: 1000.0
        val perKmRate = perKmRates[vehicleType] ?: 800.0
        val perMinuteRate = perMinuteRates[vehicleType] ?: 50.0

        var fare = baseFare + (distanceKm * perKmRate) + (estimatedMinutes * perMinuteRate)

        if (passengerCount > 1 && vehicleType in listOf(VehicleType.TUKTUK, VehicleType.CAR, VehicleType.MINIBUS)) {
            fare *= (1.0 + (passengerCount - 1) * 0.2)
        }

        return kotlin.math.floor(fare / 100) * 100
    }

    fun calculateFreightReferencePrice(distanceKm: Double, vehicleType: VehicleType, cargoType: com.ridenorth.module.freight.CargoType): Double {
        val baseRate = perKmRates[vehicleType] ?: 5000.0
        var price = distanceKm * baseRate

        val cargoMultiplier = when (cargoType) {
            com.ridenorth.module.freight.CargoType.PERISHABLE -> 1.3
            com.ridenorth.module.freight.CargoType.HAZARDOUS -> 1.5
            com.ridenorth.module.freight.CargoType.AGRICULTURAL -> 1.0
            else -> 1.0
        }
        price *= cargoMultiplier

        return kotlin.math.floor(price / 500) * 500
    }

    private fun calculateDistanceKm(from: Point, to: Point): Double {
        return from.distance(to) / 1000.0
    }

    private fun estimateDurationMinutes(distanceKm: Double): Double {
        val averageSpeedKmh = 30.0
        return (distanceKm / averageSpeedKmh) * 60.0
    }
}
