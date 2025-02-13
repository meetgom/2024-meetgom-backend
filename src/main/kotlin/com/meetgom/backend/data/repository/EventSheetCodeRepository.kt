package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.event_sheet.EventSheetCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventSheetCodeRepository : JpaRepository<EventSheetCodeEntity, String> {
    @Query("SELECT ec FROM event_sheet_code ec WHERE ec.eventSheetCode = :eventSheetCodeValue")
    fun findByEventSheetCode(@Param("eventSheetCodeValue") eventSheetCodeValue: String): EventSheetCodeEntity?
}