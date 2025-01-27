package com.meetgom.backend.data.repository

import com.meetgom.backend.data.entity.participant.ParticipantRoleEntity
import com.meetgom.backend.type.ParticipantRoleType
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRoleRepository : JpaRepository<ParticipantRoleEntity, ParticipantRoleType> {
}