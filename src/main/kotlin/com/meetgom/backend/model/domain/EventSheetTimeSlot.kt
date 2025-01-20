package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.entity.event_sheet.EventSheetTimeSlotEntity
import com.meetgom.backend.entity.event_sheet.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.model.http.response.EventSheetTimeSlotResponse
import com.meetgom.backend.utils.TimeUtils
import com.meetgom.backend.utils.extends.*
import java.time.LocalDate
import java.time.LocalTime

data class EventSheetTimeSlot(
    val eventSheetId: Long? = null,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) : Comparable<EventSheetTimeSlot> {
    override fun compareTo(other: EventSheetTimeSlot): Int {
        return compareValuesBy(this, other, { it.date }, { it.startTime }, { it.endTime })
    }

    fun convertTimeZone(
        from: TimeZone,
        to: TimeZone
    ): List<EventSheetTimeSlot> {
        val startDateTime = date.atTime(startTime)
            .changeWithTimeZone(from = from, to = to)
        val endDateTime = date.atTime(endTime)
            .changeWithTimeZone(from = from, to = to)
        val dateRange = startDateTime.untilDays(endDateTime)

        return dateRange
            .mapIndexed { idx, date ->
                EventSheetTimeSlot(
                    eventSheetId = this.eventSheetId,
                    date = date,
                    startTime = if (idx == 0) startDateTime.toLocalTime() else LocalTime.MIN,
                    endTime = if (idx == dateRange.size - 1) endDateTime.toLocalTime() else LocalTime.MAX
                )
            }
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
