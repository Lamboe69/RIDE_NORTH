package com.ridenorth.module.auth

import com.ridenorth.common.dto.AuthResponse
import com.ridenorth.common.dto.PhoneOtpRequest
import com.ridenorth.common.dto.VerifyOtpRequest
import com.ridenorth.module.driver.DriverProfile
import com.ridenorth.module.driver.DriverProfileRepository
import com.ridenorth.module.user.User
import com.ridenorth.module.user.UserRepository
import com.ridenorth.module.user.UserRole
import com.ridenorth.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val otpService: OtpService,
    private val userRepository: UserRepository,
    private val driverProfileRepository: DriverProfileRepository,
    private val notificationService: com.ridenorth.module.notification.NotificationService
) {

    fun requestOtp(request: PhoneOtpRequest): String {
        val otp = otpService.generateOtp(request.phoneNumber)
        notificationService.sendOtpSms(request.phoneNumber, otp)
        return "OTP sent successfully"
    }

    @Transactional
    fun verifyOtpAndAuthenticate(request: VerifyOtpRequest): AuthResponse {
        if (!otpService.verifyOtp(request.phoneNumber, request.otp)) {
            throw IllegalArgumentException("Invalid or expired OTP")
        }

        val user = otpService.findOrCreateUser(request.phoneNumber, "User", UserRole.RIDER)
        val userDetails = org.springframework.security.core.userdetails.User
            .withUsername(user.phoneNumber)
            .password("")
            .authorities(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_${user.role.name}"))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.isActive)
            .build()

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(userDetails.username, null, userDetails.authorities)
        )

        val token = jwtTokenProvider.generateToken(userDetails, user.id!!)
        val refreshToken = UUID.randomUUID().toString()

        return AuthResponse(
            token = token,
            refreshToken = refreshToken,
            user = toUserDto(user)
        )
    }

    @Transactional
    fun registerDriver(request: RegisterDriverRequest): AuthResponse {
        if (!otpService.verifyOtp(request.phoneNumber, request.otp)) {
            throw IllegalArgumentException("Invalid or expired OTP")
        }

        val user = otpService.findOrCreateUser(request.phoneNumber, request.name, UserRole.DRIVER)

        val existing = driverProfileRepository.findByUserId(user.id!!)
        if (existing.isEmpty) {
            val profile = DriverProfile(
                user = user,
                licenseNumber = request.licenseNumber,
                kycStatus = com.ridenorth.module.driver.KycStatus.PENDING
            )
            driverProfileRepository.save(profile)
        }

        val userDetails = org.springframework.security.core.userdetails.User
            .withUsername(user.phoneNumber)
            .password("")
            .authorities(org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_${user.role.name}"))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.isActive)
            .build()

        val token = jwtTokenProvider.generateToken(userDetails, user.id!!)
        val refreshToken = UUID.randomUUID().toString()

        return AuthResponse(
            token = token,
            refreshToken = refreshToken,
            user = toUserDto(user)
        )
    }

    @Transactional(readOnly = true)
    fun getCurrentUser(phoneNumber: String): User {
        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }
    }

    fun toUserDto(user: User): com.ridenorth.common.dto.UserDto {
        return com.ridenorth.common.dto.UserDto(
            id = user.id.toString(),
            phoneNumber = user.phoneNumber,
            name = user.name,
            role = user.role,
            ratingAvg = user.ratingAvg,
            ratingCount = user.ratingCount,
            preferredLanguage = user.preferredLanguage,
            isActive = user.isActive
        )
    }
}
