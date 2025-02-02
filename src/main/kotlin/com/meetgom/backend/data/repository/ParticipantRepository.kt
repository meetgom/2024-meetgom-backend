package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.participant.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ParticipantRepository : JpaRepository<ParticipantEntity, Long> {
    @Query("SELECT p FROM participant p WHERE p.eventSheetEntity.eventCodeEntity.eventCode = :eventSheetCode")
    fun findByEventSheetCode(@Param("eventSheetCode") eventSheetCode: String): List<ParticipantEntity>
}