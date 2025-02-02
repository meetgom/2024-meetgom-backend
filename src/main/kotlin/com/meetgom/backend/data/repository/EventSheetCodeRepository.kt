package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.event_sheet.EventCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventSheetCodeRepository : JpaRepository<EventCodeEntity, String> {
    @Query("SELECT ec FROM event_code ec WHERE ec.eventCode = :eventSheetCodeValue")
    fun findByEventSheetCode(@Param("eventSheetCodeValue") eventSheetCodeValue: String): EventCodeEntity?
}