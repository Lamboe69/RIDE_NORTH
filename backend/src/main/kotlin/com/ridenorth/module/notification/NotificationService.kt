package com.ridenorth.module.notification

import com.ridenorth.module.booking.RideRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class NotificationService(
    @Value("\${app.africastalking.api-key}") private val africasTalkingApiKey: String,
    @Value("\${app.africastalking.username}") private val africasTalkingUsername: String
) {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://api.africastalking.com/version1")
        .defaultHeader("apiKey", africasTalkingApiKey)
        .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
        .build()

    fun sendOtpSms(phoneNumber: String, otp: String) {
        val message = "Your RideNorth OTP is: $otp. Valid for 5 minutes."
        sendSms(phoneNumber, message)
    }

    fun sendSms(phoneNumber: String, message: String) {
        try {
            webClient.post()
                .uri("/messaging")
                .bodyValue(
                    "username=$africasTalkingUsername" +
                            "&to=$phoneNumber" +
                            "&message=${URLEncoder.encode(message, StandardCharsets.UTF_8)}"
                )
                .retrieve()
                .bodyToMono(String::class.java)
                .subscribe()
        } catch (e: Exception) {
            println("Failed to send SMS: ${e.message}")
        }
    }

    fun notifyDriverOfRideRequest(driver: com.ridenorth.module.driver.DriverProfile, rideRequest: RideRequest) {
        val message = "RideNorth: New ride request from ${rideRequest.pickupAddress} to ${rideRequest.dropoffAddress}. Fare: UGX ${rideRequest.fareEstimate.toInt()}. Tap to accept."
        driver.user?.phoneNumber?.let { sendSms(it, message) }
    }

    fun notifyRiderNoDriversFound(rideRequest: RideRequest) {
        val message = "RideNorth: No drivers found nearby for your ride from ${rideRequest.pickupAddress}. Please try again in a few minutes."
        rideRequest.rider?.phoneNumber?.let { sendSms(it, message) }
    }

    fun notifyRiderDriverAccepted(riderPhone: String, driverName: String, vehiclePlate: String) {
        val message = "RideNorth: Driver $driverName ($vehiclePlate) accepted your ride request. They are on the way."
        sendSms(riderPhone, message)
    }
}
