package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.ParticipantRepository
import com.meetgom.backend.domain.model.participant.Participant
import com.meetgom.backend.domain.model.participant.ParticipantAvailableTimeSlot
import com.meetgom.backend.domain.service.common.CommonEventSheetService
import com.meetgom.backend.domain.service.common.CommonParticipantService
import com.meetgom.backend.domain.service.common.CommonUserService
import com.meetgom.backend.type.ParticipantRoleType
import com.meetgom.backend.type.UserType
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ParticipantService(
    private val commonEventSheetService: CommonEventSheetService,
    private val commonParticipantService: CommonParticipantService,
    private val commonUserService: CommonUserService,
    private val participantRepository: ParticipantRepository
) {

    @Transactional
    fun createAnonymousParticipant(
        eventSheetCode: String,
        userName: String,
        password: String,
        region: String,
        availableTimeSlots: List<ParticipantAvailableTimeSlot>,
    ): Participant {
        val eventSheetEntity = commonEventSheetService.findEventSheetEntityByCodeWithException(eventSheetCode)
        val timeZone = commonEventSheetService.findTimeZoneEntityByRegionWithException(region = region).toDomain()
        val anonymousUserEntity = commonUserService.createUser(
            userType = UserType.ANONYMOUS,
            userName = userName,
            password = password
        )
        val role = ParticipantRoleType.PARTICIPANT
        val participantRoleEntity = commonParticipantService.findPariticipantRoleEntityByRoleTypeWithException(role)
        val participantEntity = Participant(
            eventSheetCode = eventSheetCode,
            user = anonymousUserEntity.toDomain(),
            role = role,
            timeZone = timeZone,
            availableTimeSlots = availableTimeSlots
        ).toEntity(
            eventSheetEntity = eventSheetEntity,
            userEntity = anonymousUserEntity,
            participantRoleEntity = participantRoleEntity
        )
        return participantRepository.save(participantEntity).toDomain()
    }
}