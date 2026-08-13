package com.ridenorth.module.notification

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface UssdSessionRepository : JpaRepository<UssdSession, UUID> {
    fun findByPhoneNumber(phoneNumber: String): Optional<UssdSession>
    fun findByPhoneNumberAndExpiresAtGreaterThan(phoneNumber: String, expiresAt: Instant): Optional<UssdSession>
    fun deleteByExpiresAtBefore(expiresAt: Instant)

    @Modifying
    @Query("DELETE FROM UssdSession u WHERE u.expiresAt < :expiresAt")
    fun deleteExpired(@Param("expiresAt") expiresAt: Instant): Int
}
