package com.ridenorth.module.application

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DriverApplicationRepository : JpaRepository<DriverApplication, UUID> {
    fun findByPhoneNumberOrderByCreatedAtDesc(phoneNumber: String): List<DriverApplication>
    fun findByStatus(status: ApplicationStatus): List<DriverApplication>
    fun findByStatusOrderByCreatedAtDesc(status: ApplicationStatus): List<DriverApplication>
    fun findAllByOrderByCreatedAtDesc(): List<DriverApplication>
    fun findByApplicationRef(applicationRef: String): Optional<DriverApplication>
}
