package com.ridenorth.module.user

import com.ridenorth.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "users", indexes = [
    Index(name = "idx_users_phone", columnList = "phone_number", unique = true),
    Index(name = "idx_users_role", columnList = "role")
])
class User(
    @Column(nullable = false, unique = true, length = 20)
    var phoneNumber: String = "",

    @Column(nullable = false)
    var name: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var role: UserRole = UserRole.RIDER,

    @Column(name = "nin_number", length = 20)
    var ninNumber: String? = null,

    @Column(nullable = false)
    var ratingAvg: Double = 5.0,

    @Column(nullable = false)
    var ratingCount: Int = 0,

    @Column(length = 500)
    var profilePhotoUrl: String? = null,

    @Column(length = 2)
    var preferredLanguage: String = "en",

    @Column(nullable = false)
    var isActive: Boolean = true,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    override var createdAt: Instant? = null
) : BaseEntity()

enum class UserRole {
    RIDER,
    DRIVER,
    SHIPPER,
    ADMIN,
    OPERATOR
}
