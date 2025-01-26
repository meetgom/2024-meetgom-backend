package com.meetgom.backend.model.domain.participant

import com.meetgom.backend.entity.participant.ParticipantAvailableTimeSlotEntity
import com.meetgom.backend.entity.participant.ParticipantAvailableTimeSlotPrimaryKey
import com.meetgom.backend.entity.participant.ParticipantEntity
import com.meetgom.backend.model.domain.common.TimeZone
import com.meetgom.backend.utils.extends.changeWithTimeZone
import com.meetgom.backend.utils.extends.untilDays
import java.time.LocalDate
import java.time.LocalTime

data class ParticipantAvailableTimeSlot(
    val participantId: Long,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) : Comparable<ParticipantAvailableTimeSlot> {
    override fun compareTo(other: ParticipantAvailableTimeSlot): Int {
        return compareValuesBy(this, other, { it.date }, { it.startTime }, { it.endTime })
    }

    fun convertTimeZone(
        from: TimeZone,
        to: TimeZone
    ): List<ParticipantAvailableTimeSlot> {
        val startDateTime = date.atTime(startTime)
            .changeWithTimeZone(from = from, to = to)
        val endDateTime = date.atTime(endTime)
            .changeWithTimeZone(from = from, to = to)
        val dateRange = startDateTime.untilDays(endDateTime)

        return dateRange
            .mapIndexed { idx, date ->
                ParticipantAvailableTimeSlot(
                    participantId = this.participantId,
                    date = date,
                    startTime = if (idx == 0) startDateTime.toLocalTime() else LocalTime.MIN,
                    endTime = if (idx == dateRange.size - 1) endDateTime.toLocalTime() else LocalTime.MAX
                )
            }
    }

    // MARK: - Converters
    fun toEntity(participantEntity: ParticipantEntity? = null): ParticipantAvailableTimeSlotEntity {
        return ParticipantAvailableTimeSlotEntity(
            participantAvailableTimeSlotPrimaryKey = ParticipantAvailableTimeSlotPrimaryKey(
                participantId = this.participantId,
                date = this.date,
                startTime = this.startTime,
            ),
            endTime = this.endTime,
            participantEntity = participantEntity
        )
    }
}