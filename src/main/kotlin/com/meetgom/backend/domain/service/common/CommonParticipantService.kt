package com.meetgom.backend.domain.service.common

import com.meetgom.backend.data.entity.participant.ParticipantRoleEntity
import com.meetgom.backend.data.repository.ParticipantRoleRepository
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.type.ParticipantRoleType
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface CommonParticipantService {
    fun findPariticipantRoleEntityByRoleTypeWithException(roleType: ParticipantRoleType): ParticipantRoleEntity
}

@Service("commonParticipantServiceImpl")
class CommonParticipantServiceImpl(
    private val participantRoleRepository: ParticipantRoleRepository,
) : CommonParticipantService {
    override fun findPariticipantRoleEntityByRoleTypeWithException(roleType: ParticipantRoleType): ParticipantRoleEntity {
        return participantRoleRepository.findById(roleType).getOrNull()
            ?: throw EventSheetExceptions.PARTICIPANT_ROLE_NOT_FOUND.toException()
    }
}