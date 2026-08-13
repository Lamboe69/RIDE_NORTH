package com.ridenorth.module.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RatingRepository : JpaRepository<Rating, UUID> {
    fun findByTripId(tripId: UUID): Optional<Rating>
    fun findByRateeId(rateeId: UUID): List<Rating>
    fun findByRaterId(raterId: UUID): List<Rating>

    @Query("""
        SELECT AVG(r.score) FROM Rating r
        WHERE r.ratee.id = :rateeId
    """)
    fun averageScoreForRatee(@Param("rateeId") rateeId: UUID): Double?
}
