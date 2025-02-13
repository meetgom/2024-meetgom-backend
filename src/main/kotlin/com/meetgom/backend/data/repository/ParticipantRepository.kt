package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.participant.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ParticipantRepository : JpaRepository<ParticipantEntity, Long> {
    @Query("SELECT p FROM participant p WHERE p.eventSheetEntity.eventSheetCodeEntity.eventSheetCode = :eventSheetCode")
    fun findByEventSheetCode(@Param("eventSheetCode") eventSheetCode: String): List<ParticipantEntity>

    @Modifying
    @Query("UPDATE user u SET u.deletedAt = CURRENT_TIMESTAMP WHERE  u.userType = 'ANONYMOUS' AND u.id IN (SELECT p.userEntity.id FROM participant p WHERE p.eventSheetEntity.eventSheetCodeEntity.eventSheetCode = :eventSheetCode)")
    fun deleteAnonymousUserEntityByEventSheetCode(@Param("eventSheetCode") eventSheetCode: String): Int?
}

//update participant p join user u on u.id=p.user_id join event_sheet e on e.id=p.event_sheet_id set deleted_at=current_timestamp(6) where ese1_0.event_sheet_code=? and ue1_0.user_type='ANONYMOUS'