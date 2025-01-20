package com.meetgom.backend.repository

import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventSheetRepository : JpaRepository<EventSheetEntity, Long> {
    @Query("SELECT e FROM event_sheet e WHERE e.eventCode.eventCode = :eventCode")
    fun findByEventCode(@Param("eventCode") eventCode: String): EventSheetEntity?
}