package com.meetgom.backend.domain.model.event_sheet

import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetTimeSlotEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.controller.http.response.EventSheetTimeSlotResponse
import com.meetgom.backend.domain.model.participant.ParticipantAvailableTimeSlot
import com.meetgom.backend.utils.utils.TimeUtils
import com.meetgom.backend.utils.extends.*
import java.time.LocalDate
import java.time.LocalTime

data class EventSheetTimeSlot(
    val eventSheetId: Long? = null,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) : Comparable<EventSheetTimeSlot> {
    companion object {
        fun comparatorForSpecificDates(): Comparator<EventSheetTimeSlot> {
            return compareBy({ it.date }, { it.startTime }, { it.endTime })
        }

        fun comparatorForRecurringWeekdays(): Comparator<EventSheetTimeSlot> {
            return compareBy({ it.date.dayOfWeek.ordinal }, { it.startTime }, { it.endTime })
        }
    }

    override fun compareTo(other: EventSheetTimeSlot): Int {
        return compareValuesBy(this, other, { it.date }, { it.startTime }, { it.endTime })
    }

    fun convertTimeZone(
        from: TimeZone,
        to: TimeZone
    ): List<EventSheetTimeSlot> {
        val startDateTime = date.atTime(startTime)
            .changeWithTimeZone(from = from, to = to)
        var endDateTime = date.atTime(endTime)
            .changeWithTimeZone(from = from, to = to)
        if (endDateTime.toLocalTime() == TimeUtils.MIN_LOCAL_TIME) {
            endDateTime = endDateTime.toLocalDate().minusDays(1).atTime(TimeUtils.MAX_LOCAL_TIME)
        }
        val dateRange = startDateTime.untilDays(endDateTime)

        val eventSheetTimeSlots = dateRange
            .mapIndexed { idx, date ->
                EventSheetTimeSlot(
                    eventSheetId = this.eventSheetId,
                    date = date,
                    startTime = if (idx == 0) startDateTime.toLocalTime() else TimeUtils.MIN_LOCAL_TIME,
                    endTime = if (idx == dateRange.size - 1) endDateTime.toLocalTime() else TimeUtils.MAX_LOCAL_TIME
                )
            }
        return eventSheetTimeSlots
    }

    fun contains(participantSlot: ParticipantAvailableTimeSlot): Boolean {
        return this.date == participantSlot.date &&
                this.startTime <= participantSlot.startTime &&
                this.endTime >= participantSlot.endTime
    }


    // MARK: - Converters
    fun toEntity(eventSheetEntity: EventSheetEntity? = null): EventSheetTimeSlotEntity {
        return EventSheetTimeSlotEntity(
            eventSheetTimeSlotPrimaryKey = EventSheetTimeSlotPrimaryKey(
                eventSheetId = this.eventSheetId,
                date = this.date,
                startTime = this.startTime,
            ),
            endTime = this.endTime,
            eventSheetEntity = eventSheetEntity
        )
    }

    fun toResponse(hideDate: Boolean = false): EventSheetTimeSlotResponse {
        return EventSheetTimeSlotResponse(
            date = if (!hideDate) this.date else null,
            dayOfWeek = this.date.dayOfWeek.name,
            startTime = TimeUtils.localTimeToTimeString(this.startTime),
            endTime = TimeUtils.localTimeToTimeString(this.endTime)
        )
    }
}
