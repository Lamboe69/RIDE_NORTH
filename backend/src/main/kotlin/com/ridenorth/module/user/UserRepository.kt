package com.ridenorth.module.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByPhoneNumber(phoneNumber: String): Optional<User>
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByRole(role: UserRole): List<User>
    fun findByRoleAndIsActiveTrue(role: UserRole): List<User>
}
