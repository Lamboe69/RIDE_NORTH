package com.ridenorth.common.dto

import com.ridenorth.module.payment.PaymentMethod
import com.ridenorth.module.payment.PaymentStatus
import com.ridenorth.module.freight.FreightJobStatus
import com.ridenorth.module.freight.QuoteStatus
import com.ridenorth.module.freight.CargoType
import com.ridenorth.module.driver.VehicleType
import java.time.Instant

data class InitiatePaymentDto(
    val tripId: String? = null,
    val freightJobId: String? = null,
    val paymentMethod: PaymentMethod,
    val amount: Double
)

data class PaymentDto(
    val id: String,
    val payerId: String,
    val payeeId: String,
    val tripId: String?,
    val freightJobId: String?,
    val amount: Double,
    val commissionAmount: Double,
    val netAmount: Double,
    val paymentMethod: PaymentMethod,
    val status: PaymentStatus,
    val providerTransactionId: String?,
    val createdAt: Instant
)

data class WalletDto(
    val id: String,
    val userId: String,
    val balance: Double,
    val pendingBalance: Double,
    val currency: String
)

data class CreateFreightJobDto(
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val dropoffLatitude: Double,
    val dropoffLongitude: Double,
    val pickupAddress: String,
    val dropoffAddress: String,
    val cargoType: CargoType,
    val estimatedWeightKg: Double,
    val estimatedVolumeM3: Double,
    val cargoDescription: String?,
    val preferredDate: Instant?,
    val preferredVehicleType: VehicleType?,
    val minPrice: Double,
    val maxPrice: Double,
    val specialInstructions: String?
)

data class CreateFreightQuoteDto(
    val freightJobId: String,
    val quotedPrice: Double,
    val estimatedDurationHours: Double,
    val message: String?
)

data class FreightJobDto(
    val id: String,
    val shipperId: String,
    val pickupLocation: LocationDto,
    val dropoffLocation: LocationDto,
    val pickupAddress: String,
    val dropoffAddress: String,
    val cargoType: CargoType,
    val estimatedWeightKg: Double,
    val status: FreightJobStatus,
    val minPrice: Double,
    val maxPrice: Double,
    val createdAt: Instant
)

data class FreightQuoteDto(
    val id: String,
    val freightJobId: String,
    val driverId: String,
    val quotedPrice: Double,
    val estimatedDurationHours: Double,
    val message: String?,
    val status: QuoteStatus,
    val createdAt: Instant
)
