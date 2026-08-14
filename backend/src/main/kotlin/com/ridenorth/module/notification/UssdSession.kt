package com.ridenorth.module.notification

import com.ridenorth.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant

@Entity
@Table(name = "ussd_sessions", indexes = [
    Index(name = "idx_ussd_phone", columnList = "phone_number"),
    Index(name = "idx_ussd_state", columnList = "session_state")
])
class UssdSession(
    @Column(nullable = false, length = 20)
    var phoneNumber: String = "",

    @Column(nullable = false, length = 50)
    var sessionState: String = "",

    @JdbcTypeCode(SqlTypes.JSON)
    var sessionData: Map<String, String> = emptyMap(),

    @Column(nullable = false)
    var lastAction: String = "",

    @Column(nullable = false)
    var expiresAt: Instant? = null,

    @Column(nullable = false)
    override var createdAt: Instant? = null
) : BaseEntity()
