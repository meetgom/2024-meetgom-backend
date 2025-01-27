package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.event_sheet.EventCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventCodeRepository : JpaRepository<EventCodeEntity, String> {
    @Query("SELECT COUNT(ec) > 0 FROM event_code ec WHERE ec.eventCode = :eventCode")
    fun existsByEventCode(@Param("eventCode") eventCode: String): Boolean
}