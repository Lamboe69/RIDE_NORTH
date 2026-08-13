package com.ridenorth.module.freight

import com.ridenorth.common.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/freight")
class FreightController(private val freightJobService: com.ridenorth.module.freight.FreightJobService) {

    @PostMapping("/jobs")
    @PreAuthorize("hasRole('SHIPPER') or hasRole('RIDER')")
    fun createFreightJob(@Valid @RequestBody request: CreateFreightJobDto): ResponseEntity<FreightJobDto> {
        val job = freightJobService.createFreightJob(request)
        return ResponseEntity.ok(toDto(job))
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('SHIPPER') or hasRole('RIDER')")
    fun getMyFreightJobs(): ResponseEntity<List<FreightJobDto>> {
        val jobs = freightJobService.getMyFreightJobs()
        return ResponseEntity.ok(jobs.map { toDto(it) })
    }

    @GetMapping("/jobs/{jobId}")
    fun getFreightJob(@PathVariable jobId: UUID): ResponseEntity<FreightJobDto> {
        val job = freightJobService.getFreightJobById(jobId)
        return ResponseEntity.ok(toDto(job))
    }

    @PostMapping("/jobs/{jobId}/quotes")
    @PreAuthorize("hasRole('DRIVER')")
    fun createFreightQuote(@PathVariable jobId: UUID, @Valid @RequestBody request: CreateFreightQuoteDto): ResponseEntity<FreightQuoteDto> {
        val quote = freightJobService.createQuote(jobId, request)
        return ResponseEntity.ok(toQuoteDto(quote))
    }

    @GetMapping("/jobs/{jobId}/quotes")
    fun getQuotesForJob(@PathVariable jobId: UUID): ResponseEntity<List<FreightQuoteDto>> {
        val quotes = freightJobService.getQuotesForJob(jobId)
        return ResponseEntity.ok(quotes.map { toQuoteDto(it) })
    }

    private fun toDto(job: FreightJob): FreightJobDto {
        return FreightJobDto(
            id = job.id.toString(),
            shipperId = job.shipper?.id.toString(),
            pickupLocation = toLocationDto(job.pickupLocation!!),
            dropoffLocation = toLocationDto(job.dropoffLocation!!),
            pickupAddress = job.pickupAddress,
            dropoffAddress = job.dropoffAddress,
            cargoType = job.cargoType,
            estimatedWeightKg = job.estimatedWeightKg,
            status = job.status,
            minPrice = job.minPrice,
            maxPrice = job.maxPrice,
            createdAt = job.createdAt!!
        )
    }

    private fun toQuoteDto(quote: FreightQuote): FreightQuoteDto {
        return FreightQuoteDto(
            id = quote.id.toString(),
            freightJobId = quote.freightJob?.id.toString(),
            driverId = quote.driver?.id.toString(),
            quotedPrice = quote.quotedPrice,
            estimatedDurationHours = quote.estimatedDurationHours,
            message = quote.message,
            status = quote.status,
            createdAt = quote.createdAt!!
        )
    }

    private fun toLocationDto(point: org.locationtech.jts.geom.Point): com.ridenorth.common.dto.LocationDto {
        return com.ridenorth.common.dto.LocationDto(
            latitude = point.y,
            longitude = point.x
        )
    }
}
