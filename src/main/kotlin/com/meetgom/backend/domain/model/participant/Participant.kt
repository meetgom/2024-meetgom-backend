package com.meetgom.backend.domain.model.participant

import com.meetgom.backend.controller.http.response.ParticipantResponse
import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.participant.ParticipantEntity
import com.meetgom.backend.data.entity.participant.ParticipantRoleEntity
import com.meetgom.backend.data.entity.user.UserEntity
import com.meetgom.backend.exception.exceptions.AuthExceptions
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.domain.model.user.User
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.type.ParticipantRoleType
import com.meetgom.backend.utils.extends.sorted

data class Participant(
    val eventSheetCode: String,
    val user: User,
    val role: ParticipantRoleType,
    val timeZone: TimeZone,
    val availableTimeSlots: List<ParticipantAvailableTimeSlot>
) {
    fun convertTimeZone(
        to: TimeZone,
        eventSheetType: EventSheetType
    ): Participant {
        val availableTimeSlots = this.availableTimeSlots.map {
            it.convertTimeZone(
                from = this.timeZone,
                to = timeZone,
            )
        }.flatten().sorted(eventSheetType = eventSheetType)

        return Participant(
            eventSheetCode = eventSheetCode,
            user = user,
            role = role,
            timeZone = to,
            availableTimeSlots = availableTimeSlots,
        )
    }

    private fun convertSystemDefaultTimeZone(eventSheetType: EventSheetType): Participant {
        return this.convertTimeZone(TimeZone.defaultTimeZone, eventSheetType = eventSheetType)
    }

    fun toEntity(
        eventSheetEntity: EventSheetEntity,
        userEntity: UserEntity,
        participantRoleEntity: ParticipantRoleEntity
    ): ParticipantEntity {
        val participant = this.convertSystemDefaultTimeZone(eventSheetType = eventSheetEntity.eventSheetType)
        if (eventSheetEntity.eventSheetCodeEntity.eventCode != participant.eventSheetCode)
            throw EventSheetExceptions.UNMATCHED_EVENT_SHEET_CODE.toException()
        if (userEntity.id != participant.user.id)
            throw AuthExceptions.UNMATCHED_USER.toException()
        if (participantRoleEntity.participantRole !== participant.role)
            throw EventSheetExceptions.UNMATCHED_ROLE_TYPE.toException()
        val participantEntity = ParticipantEntity(
            eventSheetEntity = eventSheetEntity,
            user = userEntity,
            role = participantRoleEntity,
            timeZoneEntity = participant.timeZone.toEntity(),
            availableTimeSlotEntities = mutableListOf()
        )
        val availableTimeSlotEntities =
            participant.availableTimeSlots.map { it.toEntity(participantEntity = participantEntity) }.toMutableList()
        participantEntity.availableTimeSlotEntities = availableTimeSlotEntities
        return participantEntity
    }

    fun toResponse(): ParticipantResponse {
        return ParticipantResponse(
            eventSheetCode = eventSheetCode,
            user = user.toResponse(),
            role = role,
            timeZone = timeZone.region,
            availableTimeSlots = availableTimeSlots.map { it.toResponse() }
        )
    }
}