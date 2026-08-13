package com.ridenorth.module.user

import com.ridenorth.module.driver.DriverProfileRepository
import com.ridenorth.module.driver.VehicleRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val driverProfileRepository: DriverProfileRepository,
    private val vehicleRepository: VehicleRepository
) {

    @Transactional(readOnly = true)
    fun getCurrentUserProfile(): User {
        val phoneNumber = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalStateException("Not authenticated")
        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow { IllegalArgumentException("User not found") }
    }

    @Transactional(readOnly = true)
    fun getUserById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
    }
}
