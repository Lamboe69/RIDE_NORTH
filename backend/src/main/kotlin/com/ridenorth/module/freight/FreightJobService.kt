package com.ridenorth.module.freight

import com.ridenorth.common.dto.CreateFreightJobDto
import com.ridenorth.common.dto.CreateFreightQuoteDto
import com.ridenorth.module.user.UserRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class FreightJobService(
    private val freightJobRepository: FreightJobRepository,
    private val freightQuoteRepository: FreightQuoteRepository,
    private val driverProfileRepository: com.ridenorth.module.driver.DriverProfileRepository,
    private val userRepository: UserRepository
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    @Transactional
    fun createFreightJob(request: CreateFreightJobDto): FreightJob {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val shipper = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val pickupPoint: Point = geometryFactory.createPoint(Coordinate(request.pickupLongitude, request.pickupLatitude))
        val dropoffPoint: Point = geometryFactory.createPoint(Coordinate(request.dropoffLongitude, request.dropoffLatitude))

        val job = FreightJob(
            shipper = shipper,
            pickupLocation = pickupPoint,
            dropoffLocation = dropoffPoint,
            pickupAddress = request.pickupAddress,
            dropoffAddress = request.dropoffAddress,
            cargoType = request.cargoType,
            estimatedWeightKg = request.estimatedWeightKg,
            estimatedVolumeM3 = request.estimatedVolumeM3,
            cargoDescription = request.cargoDescription,
            preferredDate = request.preferredDate,
            preferredVehicleType = request.preferredVehicleType,
            minPrice = request.minPrice,
            maxPrice = request.maxPrice,
            specialInstructions = request.specialInstructions,
            status = com.ridenorth.module.freight.FreightJobStatus.OPEN,
            createdAt = java.time.Instant.now()
        )
        return freightJobRepository.save(job)
    }

    @Transactional(readOnly = true)
    fun getMyFreightJobs(): List<FreightJob> {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }
        return freightJobRepository.findByShipperId(user.id!!)
    }

    @Transactional(readOnly = true)
    fun getFreightJobById(jobId: UUID): FreightJob {
        return freightJobRepository.findById(jobId)
            .orElseThrow { IllegalArgumentException("Freight job not found") }
    }

    @Transactional
    fun createQuote(jobId: UUID, request: CreateFreightQuoteDto): FreightQuote {
        val job = freightJobRepository.findById(jobId)
            .orElseThrow { IllegalArgumentException("Freight job not found") }

        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val user = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val driverProfile = driverProfileRepository.findByUserId(user.id!!)
            .orElseThrow { IllegalArgumentException("Driver profile not found") }

        val quote = FreightQuote(
            freightJob = job,
            driver = driverProfile,
            quotedPrice = request.quotedPrice,
            estimatedDurationHours = request.estimatedDurationHours,
            message = request.message,
            status = com.ridenorth.module.freight.QuoteStatus.PENDING,
            createdAt = java.time.Instant.now()
        )
        return freightQuoteRepository.save(quote)
    }

    @Transactional(readOnly = true)
    fun getQuotesForJob(jobId: UUID): List<FreightQuote> {
        return freightQuoteRepository.findByFreightJobId(jobId)
    }
}
