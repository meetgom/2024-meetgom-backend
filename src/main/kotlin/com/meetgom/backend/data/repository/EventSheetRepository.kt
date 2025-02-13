package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventSheetRepository : JpaRepository<EventSheetEntity, Long> {
    @Query("SELECT e FROM event_sheet e WHERE e.eventSheetCodeEntity.eventSheetCode = :eventSheetCodeValue")
    fun findByEventSheetCode(@Param("eventSheetCodeValue") eventSheetCodeValue: String): EventSheetEntity?

    @Modifying
    @Query("UPDATE event_sheet e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.eventSheetCodeEntity.eventSheetCode = :eventSheetCodeValue")
    fun deleteByEventSheetCode(@Param("eventSheetCodeValue") eventSheetCodeValue: String): Int?
}