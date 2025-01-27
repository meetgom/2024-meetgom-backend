package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.participant.ParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository : JpaRepository<ParticipantEntity, Long> {
}