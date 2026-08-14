package com.ridenorth.module.payment

import com.ridenorth.common.BaseEntity
import com.ridenorth.module.user.User
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "wallets", indexes = [
    Index(name = "idx_wallet_user", columnList = "user_id", unique = true)
])
class Wallet(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(nullable = false, precision = 12, scale = 2)
    var balance: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, precision = 12, scale = 2)
    var pendingBalance: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var currency: String = "UGX",

    @Column(nullable = false)
    var lastUpdated: Instant? = null,

    @Column(nullable = false)
    override var createdAt: Instant? = null
) : BaseEntity()
