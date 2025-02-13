package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.common.EventSheetCodeWordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventSheetCodeWordRepository : JpaRepository<EventSheetCodeWordEntity, String> {
    @Query("SELECT ecw FROM event_sheet_code_word ecw ORDER BY RAND() limit :wordCount")
    fun findRandomEventSheetCodeWords(@Param("wordCount") wordCount: Int = 3): List<EventSheetCodeWordEntity>
}