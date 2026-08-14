package com.ridenorth.common

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: java.util.UUID? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    open var createdAt: Instant? = null

    @UpdateTimestamp
    @Column(nullable = false)
    open var updatedAt: Instant? = null

    @Column(nullable = false)
    open var isDeleted: Boolean = false
}
