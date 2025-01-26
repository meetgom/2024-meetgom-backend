package com.meetgom.backend.model.domain.participant

import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.entity.participant.ParticipantEntity
import com.meetgom.backend.entity.participant.ParticipantRoleEntity
import com.meetgom.backend.entity.user.UserEntity
import com.meetgom.backend.exception.exceptions.EventSheetExceptions
import com.meetgom.backend.model.domain.common.TimeZone
import com.meetgom.backend.model.domain.user.User
import com.meetgom.backend.type.ParticipantRoleType
import com.meetgom.backend.utils.extends.alignTimeSlots

data class Participant(
    val eventSheetCode: String,
    val user: User,
    val role: ParticipantRoleType,
    val timeZone: TimeZone,
    val availableTimeSlots: List<ParticipantAvailableTimeSlot>
) {

    fun convertTimeZone(to: TimeZone): Participant {
        val availableTimeSlots = this.availableTimeSlots.map {
            it.convertTimeZone(
                from = this.timeZone,
                to = timeZone,
            )
        }.flatten().alignTimeSlots()

        return Participant(
            eventSheetCode = eventSheetCode,
            user = user,
            role = role,
            timeZone = to,
            availableTimeSlots = availableTimeSlots,
        )
    }

    private fun convertSystemDefaultTimeZone(): Participant {
        return this.convertTimeZone(TimeZone.defaultTimeZone)
    }

    fun toEntity(
        eventSheetEntity: EventSheetEntity,
        userEntity: UserEntity,
        participantRoleEntity: ParticipantRoleEntity
    ): ParticipantEntity {
        val participant = this.convertSystemDefaultTimeZone()
        if (eventSheetEntity.eventCode.eventCode != participant.eventSheetCode)
            throw EventSheetExceptions.UNMATCHED_EVENT_CODE.toException()
        if (userEntity.id != participant.user.id)
            throw EventSheetExceptions.UNMATCHED_USER.toException()
        if (participantRoleEntity.participantRole !== participant.role)
            throw EventSheetExceptions.UNMATCHED_ROLE_TYPE.toException()

        return ParticipantEntity(
            eventSheet = eventSheetEntity,
            user = userEntity,
            role = participantRoleEntity,
            timeZoneEntity = participant.timeZone.toEntity(),
            availableTimeSlotEntities = participant.availableTimeSlots.map { it.toEntity() }.toMutableList()
        )
    }
}