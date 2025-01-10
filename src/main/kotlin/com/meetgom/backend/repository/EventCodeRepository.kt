package com.meetgom.backend.repository

import com.meetgom.backend.entity.EventCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventCodeRepository : JpaRepository<EventCodeEntity, Long> {
    @Query("SELECT COUNT(ec) > 0 FROM event_code ec WHERE ec.eventCode = :eventCode")
    fun existsByPinCode(@Param("eventCode") eventCode: String): Boolean
}