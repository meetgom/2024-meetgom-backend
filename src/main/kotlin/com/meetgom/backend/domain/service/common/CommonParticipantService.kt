package com.meetgom.backend.domain.service.common

import com.meetgom.backend.data.entity.participant.ParticipantEntity
import com.meetgom.backend.data.entity.participant.ParticipantRoleEntity
import com.meetgom.backend.data.repository.ParticipantRepository
import com.meetgom.backend.data.repository.ParticipantRoleRepository
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.exception.exceptions.ParticipantExceptions
import com.meetgom.backend.type.ParticipantRoleType
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class CommonParticipantService(
    private val participantRepository: ParticipantRepository,
    private val participantRoleRepository: ParticipantRoleRepository,
) {
    fun findParticipantRoleEntityByRoleTypeWithException(roleType: ParticipantRoleType): ParticipantRoleEntity {
        return participantRoleRepository.findById(roleType).getOrNull()
            ?: throw EventSheetExceptions.PARTICIPANT_ROLE_NOT_FOUND.toException()
    }

    fun deleteAllParticipantEntityByEventSheetCode(eventSheetCode: String): Boolean {
        return (participantRepository.deleteAllParticipantEntityByEventSheetCode(eventSheetCode) ?: 0) > 0
    }

    fun deleteAnonymousUserEntityByEventSheetCode(eventSheetCode: String): Boolean {
        return (participantRepository.deleteAnonymousUserEntityByEventSheetCode(eventSheetCode) ?: 0) > 0
    }

    fun findParticipantEntityByIdWithException(participantId: Long): ParticipantEntity {
        val participantEntity = participantRepository.findById(participantId)
            .orElseThrow { ParticipantExceptions.PARTICIPANT_NOT_FOUND.toException() }
        if (participantEntity.deletedAt != null || participantEntity.userEntity.deletedAt != null) {
            throw ParticipantExceptions.PARTICIPANT_NOT_FOUND.toException()
        }
        return participantEntity
    }
}