package com.meetgom.backend.domain.model.participant

import com.meetgom.backend.controller.http.response.ParticipantAvailableTimeSlotResponse
import com.meetgom.backend.data.entity.participant.ParticipantAvailableTimeSlotEntity
import com.meetgom.backend.data.entity.participant.ParticipantAvailableTimeSlotPrimaryKey
import com.meetgom.backend.data.entity.participant.ParticipantEntity
import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.utils.TimeUtils
import com.meetgom.backend.utils.extends.changeWithTimeZone
import com.meetgom.backend.utils.extends.untilDays
import java.time.LocalDate
import java.time.LocalTime

data class ParticipantAvailableTimeSlot(
    val participantId: Long? = null,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) : Comparable<ParticipantAvailableTimeSlot> {
    companion object {
        fun comparatorForSpecificDates(): Comparator<ParticipantAvailableTimeSlot> {
            return compareBy({ it.date }, { it.startTime }, { it.endTime })
        }

        fun comparatorForRecurringWeekdays(): Comparator<ParticipantAvailableTimeSlot> {
            return compareBy({ it.date.dayOfWeek.ordinal }, { it.startTime }, { it.endTime })
        }
    }

    override fun compareTo(other: ParticipantAvailableTimeSlot): Int {
        return compareValuesBy(this, other, { it.date }, { it.startTime }, { it.endTime })
    }

    fun convertTimeZone(
        from: TimeZone,
        to: TimeZone
    ): List<ParticipantAvailableTimeSlot> {
        val startDateTime = date.atTime(startTime)
            .changeWithTimeZone(from = from, to = to)
        var endDateTime = date.atTime(endTime)
            .changeWithTimeZone(from = from, to = to)
        if (endDateTime.toLocalTime() == TimeUtils.MIN_LOCAL_TIME) {
            endDateTime = endDateTime.toLocalDate().minusDays(1).atTime(TimeUtils.MAX_LOCAL_TIME)
        }
        val dateRange = startDateTime.untilDays(endDateTime)

        val participantAvailableTimeSlots = dateRange
            .mapIndexed { idx, date ->
                ParticipantAvailableTimeSlot(
                    participantId = this.participantId,
                    date = date,
                    startTime = if (idx == 0) startDateTime.toLocalTime() else TimeUtils.MIN_LOCAL_TIME,
                    endTime = if (idx == dateRange.size - 1) endDateTime.toLocalTime() else TimeUtils.MAX_LOCAL_TIME
                )
            }
        return participantAvailableTimeSlots
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

    fun toResponse(hideDate: Boolean = false): ParticipantAvailableTimeSlotResponse {
        return ParticipantAvailableTimeSlotResponse(
            date = if (!hideDate) this.date else null,
            dayOfWeek = this.date.dayOfWeek.name,
            startTime = TimeUtils.localTimeToTimeString(this.startTime),
            endTime = TimeUtils.localTimeToTimeString(this.endTime)
        )
    }
}