package com.ridenorth.common.dto

import com.ridenorth.module.payment.PaymentMethod
import com.ridenorth.module.payment.PaymentStatus
import com.ridenorth.module.freight.FreightJobStatus
import com.ridenorth.module.freight.QuoteStatus
import com.ridenorth.module.freight.CargoType
import com.ridenorth.module.driver.VehicleType
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.Instant

data class InitiatePaymentDto(
    val tripId: String? = null,
    val freightJobId: String? = null,

    @field:NotNull(message = "Payment method is required")
    val paymentMethod: PaymentMethod,

    @field:Positive(message = "Amount must be greater than 0")
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
    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Pickup latitude is required")
    val pickupLatitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Pickup longitude is required")
    val pickupLongitude: Double,

    @field:DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @field:DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @field:NotNull(message = "Dropoff latitude is required")
    val dropoffLatitude: Double,

    @field:DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @field:DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @field:NotNull(message = "Dropoff longitude is required")
    val dropoffLongitude: Double,

    @field:NotBlank(message = "Pickup address is required")
    val pickupAddress: String,

    @field:NotBlank(message = "Dropoff address is required")
    val dropoffAddress: String,

    @field:NotNull(message = "Cargo type is required")
    val cargoType: CargoType,

    @field:Positive(message = "Estimated weight must be greater than 0")
    val estimatedWeightKg: Double,

    @field:Positive(message = "Estimated volume must be greater than 0")
    val estimatedVolumeM3: Double,

    val cargoDescription: String? = null,
    val preferredDate: Instant? = null,
    val preferredVehicleType: VehicleType? = null,

    @field:Positive(message = "Min price must be greater than 0")
    val minPrice: Double,

    @field:Positive(message = "Max price must be greater than 0")
    val maxPrice: Double,

    val specialInstructions: String? = null
)

data class CreateFreightQuoteDto(
    @field:NotBlank(message = "Freight job ID is required")
    val freightJobId: String,

    @field:Positive(message = "Quoted price must be greater than 0")
    val quotedPrice: Double,

    @field:Positive(message = "Estimated duration must be greater than 0")
    val estimatedDurationHours: Double,

    val message: String? = null
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
