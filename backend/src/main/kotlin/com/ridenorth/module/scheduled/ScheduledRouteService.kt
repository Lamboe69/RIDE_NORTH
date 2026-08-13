package com.ridenorth.module.scheduled

import com.ridenorth.common.dto.CreateScheduledRouteDto
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
class ScheduledRouteService(
    private val scheduledRouteRepository: ScheduledRouteRepository,
    private val userRepository: UserRepository
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    @Transactional
    fun createRoute(request: CreateScheduledRouteDto): ScheduledRoute {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        val operator = userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }

        val originPoint: Point = geometryFactory.createPoint(Coordinate(request.originLongitude, request.originLatitude))
        val destinationPoint: Point = geometryFactory.createPoint(Coordinate(request.destinationLongitude, request.destinationLatitude))

        val route = ScheduledRoute(
            operator = operator,
            origin = request.origin,
            destination = request.destination,
            originLocation = originPoint,
            destinationLocation = destinationPoint,
            departureTime = request.departureTime,
            seatCapacity = request.seatCapacity,
            pricePerSeat = request.pricePerSeat,
            minSeatsToConfirm = request.minSeatsToConfirm,
            notes = request.notes,
            createdAt = java.time.Instant.now()
        )
        return scheduledRouteRepository.save(route)
    }

    @Transactional(readOnly = true)
    fun searchRoutes(origin: String, destination: String): List<ScheduledRoute> {
        val now = java.time.Instant.now()
        return scheduledRouteRepository.findUpcomingRoutes(origin, destination, now)
    }

    @Transactional(readOnly = true)
    fun getRouteById(routeId: UUID): ScheduledRoute {
        return scheduledRouteRepository.findById(routeId)
            .orElseThrow { IllegalArgumentException("Route not found") }
    }

    @Transactional
    fun bookSeat(routeId: UUID): ScheduledRoute {
        val route = scheduledRouteRepository.findById(routeId)
            .orElseThrow { IllegalArgumentException("Route not found") }

        if (route.seatsBooked >= route.seatCapacity) {
            throw IllegalStateException("No seats available")
        }

        route.seatsBooked++
        return scheduledRouteRepository.save(route)
    }
}
