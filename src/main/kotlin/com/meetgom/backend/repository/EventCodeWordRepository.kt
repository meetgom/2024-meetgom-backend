package com.meetgom.backend.repository

import com.meetgom.backend.entity.EventCodeWordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventCodeWordRepository : JpaRepository<EventCodeWordEntity, Long> {
    @Query("SELECT ecw FROM event_code_word ecw ORDER BY RAND() limit :wordCount")
    fun getRandomPinCodeWords(@Param("wordCount") wordCount: Int = 3): List<EventCodeWordEntity>
}