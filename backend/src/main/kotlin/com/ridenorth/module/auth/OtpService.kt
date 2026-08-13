package com.ridenorth.module.auth

import com.ridenorth.module.user.UserRepository
import com.ridenorth.module.user.UserRole
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant
import java.util.*

@Service
class OtpService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val otpStore = mutableMapOf<String, OtpEntry>()

    data class OtpEntry(
        val otp: String,
        val expiresAt: Instant
    )

    fun generateOtp(phoneNumber: String): String {
        val otp = SecureRandom().let { sr ->
            val code = StringBuilder()
            repeat(6) { code.append(sr.nextInt(10)) }
            code.toString()
        }

        val expiresAt = Instant.now().plusSeconds(300)
        otpStore[phoneNumber] = OtpEntry(otp, expiresAt)
        return otp
    }

    fun verifyOtp(phoneNumber: String, otp: String): Boolean {
        val entry = otpStore[phoneNumber]
        if (entry == null || entry.expiresAt.isBefore(Instant.now())) {
            otpStore.remove(phoneNumber)
            return false
        }
        val isValid = entry.otp == otp
        if (isValid) {
            otpStore.remove(phoneNumber)
        }
        return isValid
    }

    fun cleanupExpiredOtps() {
        val now = Instant.now()
        otpStore.entries.removeIf { it.value.expiresAt.isBefore(now) }
    }

    @Transactional
    fun findOrCreateUser(phoneNumber: String, name: String, role: UserRole): com.ridenorth.module.user.User {
        return userRepository.findByPhoneNumber(phoneNumber).orElseGet {
            val user = com.ridenorth.module.user.User(
                phoneNumber = phoneNumber,
                name = name,
                role = role,
                isActive = true
            )
            userRepository.save(user)
        }
    }
}
