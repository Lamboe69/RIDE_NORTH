package com.ridenorth.module.scheduled

import com.ridenorth.common.dto.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/scheduled")
class ScheduledController(private val scheduledRouteService: ScheduledRouteService) {

    @PostMapping("/routes")
    @PreAuthorize("hasRole('OPERATOR') or hasRole('ADMIN')")
    fun createRoute(@Valid @RequestBody request: CreateScheduledRouteDto): ResponseEntity<ScheduledRouteDto> {
        val route = scheduledRouteService.createRoute(request)
        return ResponseEntity.ok(toDto(route))
    }

    @GetMapping("/routes")
    fun searchRoutes(
        @RequestParam origin: String,
        @RequestParam destination: String
    ): ResponseEntity<List<ScheduledRouteDto>> {
        val routes = scheduledRouteService.searchRoutes(origin, destination)
        return ResponseEntity.ok(routes.map { toDto(it) })
    }

    @GetMapping("/routes/{routeId}")
    fun getRoute(@PathVariable routeId: UUID): ResponseEntity<ScheduledRouteDto> {
        val route = scheduledRouteService.getRouteById(routeId)
        return ResponseEntity.ok(toDto(route))
    }

    @PostMapping("/routes/{routeId}/book")
    @PreAuthorize("hasRole('RIDER')")
    fun bookSeat(@PathVariable routeId: UUID): ResponseEntity<ScheduledRouteDto> {
        val route = scheduledRouteService.bookSeat(routeId)
        return ResponseEntity.ok(toDto(route))
    }

    private fun toDto(route: ScheduledRoute): ScheduledRouteDto {
        return ScheduledRouteDto(
            id = route.id.toString(),
            operatorId = route.operator?.id.toString(),
            origin = route.origin,
            destination = route.destination,
            departureTime = route.departureTime,
            seatCapacity = route.seatCapacity,
            seatsBooked = route.seatsBooked,
            pricePerSeat = route.pricePerSeat,
            minSeatsToConfirm = route.minSeatsToConfirm,
            status = route.status,
            notes = route.notes,
            createdAt = route.createdAt
        )
    }
}
