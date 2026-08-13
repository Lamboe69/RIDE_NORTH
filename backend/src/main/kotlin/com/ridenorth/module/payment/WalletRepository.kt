package com.ridenorth.module.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository : JpaRepository<Wallet, UUID> {
    fun findByUserId(userId: UUID): Optional<Wallet>
    fun existsByUserId(userId: UUID): Boolean
}
