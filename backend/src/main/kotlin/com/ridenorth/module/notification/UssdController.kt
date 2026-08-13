package com.ridenorth.module.notification

import com.ridenorth.module.notification.UssdSessionRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/ussd")
class UssdController(private val ussdSessionRepository: UssdSessionRepository) {

    @PostMapping("/handle")
    fun handleUssd(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, String>> {
        val phoneNumber = request["phoneNumber"] ?: return ResponseEntity.ok(ussdResponse("END", "Invalid request"))
        val sessionId = request["sessionId"] ?: UUID.randomUUID().toString()
        val serviceCode = request["serviceCode"] ?: "*123#"
        val text = request["text"] ?: ""

        val session = ussdSessionRepository.findByPhoneNumberAndExpiresAtGreaterThan(phoneNumber, Instant.now())
            .orElseGet {
                ussdSessionRepository.save(
                    UssdSession(
                        phoneNumber = phoneNumber,
                        sessionState = "MAIN_MENU",
                        lastAction = "start",
                        expiresAt = Instant.now().plusSeconds(600)
                    )
                )
            }

        val response = processUssdFlow(session, text)
        ussdSessionRepository.save(session)
        return ResponseEntity.ok(ussdResponse(response.first, response.second))
    }

    private fun processUssdFlow(session: UssdSession, text: String): Pair<String, String> {
        val parts = text.split("*").filter { it.isNotBlank() }
        val input = if (parts.isEmpty()) "" else parts.last()

        return when (session.sessionState) {
            "MAIN_MENU" -> {
                when (input) {
                    "" -> Pair("CON", "Welcome to RideNorth\n1. Book a ride\n2. Check ride status\n3. Freight booking\n0. Exit")
                    "1" -> {
                        session.sessionState = "BOOK_RIDE_PICKUP"
                        Pair("CON", "Enter pickup location:")
                    }
                    "2" -> {
                        session.sessionState = "CHECK_STATUS"
                        Pair("CON", "Enter your ride request ID:")
                    }
                    "3" -> {
                        session.sessionState = "FREIGHT_MAIN"
                        Pair("CON", "Freight Booking\n1. Post a job\n2. Check my jobs\n0. Back")
                    }
                    "0" -> Pair("END", "Thank you for using RideNorth")
                    else -> Pair("END", "Invalid option")
                }
            }
            "BOOK_RIDE_PICKUP" -> {
                session.sessionData = mapOf("pickup" to input)
                session.sessionState = "BOOK_RIDE_DROPOFF"
                Pair("CON", "Enter destination:")
            }
            "BOOK_RIDE_DROPOFF" -> {
                session.sessionData = session.sessionData + ("dropoff" to input)
                session.sessionState = "BOOK_RIDE_VEHICLE"
                Pair("CON", "Select vehicle:\n1. Boda Boda\n2. Tuk Tuk\n3. Car\n0. Cancel")
            }
            "BOOK_RIDE_VEHICLE" -> {
                val vehicleType = when (input) {
                    "1" -> "BODA"
                    "2" -> "TUKTUK"
                    "3" -> "CAR"
                    "0" -> {
                        session.sessionState = "MAIN_MENU"
                        return Pair("CON", "Booking cancelled. Main Menu:\n1. Book a ride\n2. Check ride status\n0. Exit")
                    }
                    else -> return Pair("END", "Invalid vehicle selection")
                }
                session.sessionData = session.sessionData + ("vehicleType" to vehicleType)
                session.sessionState = "BOOK_RIDE_CONFIRM"
                val pickup = session.sessionData["pickup"] ?: "Unknown"
                val dropoff = session.sessionData["dropoff"] ?: "Unknown"
                Pair("CON", "Confirm booking:\nPickup: $pickup\nDropoff: $dropoff\nVehicle: $vehicleType\n\nReply 1 to confirm, 0 to cancel")
            }
            "BOOK_RIDE_CONFIRM" -> {
                if (input == "1") {
                    session.sessionState = "MAIN_MENU"
                    Pair("END", "Your ride request has been created. You will receive an SMS with driver details shortly.")
                } else {
                    session.sessionState = "MAIN_MENU"
                    Pair("END", "Booking cancelled.")
                }
            }
            "CHECK_STATUS" -> {
                session.sessionState = "MAIN_MENU"
                Pair("END", "Your ride is in progress. Driver will arrive shortly.")
            }
            "FREIGHT_MAIN" -> {
                when (input) {
                    "1" -> {
                        session.sessionState = "FREIGHT_PICKUP"
                        Pair("CON", "Enter pickup location:")
                    }
                    "2" -> {
                        session.sessionState = "MAIN_MENU"
                        Pair("END", "No active freight jobs found.")
                    }
                    "0" -> {
                        session.sessionState = "MAIN_MENU"
                        Pair("CON", "Main Menu:\n1. Book a ride\n2. Check ride status\n3. Freight booking\n0. Exit")
                    }
                    else -> Pair("END", "Invalid option")
                }
            }
            "FREIGHT_PICKUP" -> {
                session.sessionData = mapOf("pickup" to input)
                session.sessionState = "FREIGHT_DROPOFF"
                Pair("CON", "Enter destination:")
            }
            "FREIGHT_DROPOFF" -> {
                session.sessionData = session.sessionData + ("dropoff" to input)
                session.sessionState = "FREIGHT_CARGO"
                Pair("CON", "Enter cargo type:\n1. Agricultural\n2. General\n3. Construction\n0. Cancel")
            }
            "FREIGHT_CARGO" -> {
                session.sessionState = "MAIN_MENU"
                Pair("END", "Freight job created. Drivers will contact you with quotes.")
            }
            else -> {
                session.sessionState = "MAIN_MENU"
                Pair("CON", "Welcome to RideNorth\n1. Book a ride\n2. Check ride status\n3. Freight booking\n0. Exit")
            }
        }
    }

    private fun ussdResponse(type: String, message: String): Map<String, String> {
        return mapOf(
            "type" to type,
            "message" to message
        )
    }
}
