package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.entity.EventSheetTimeSlotEntity
import com.meetgom.backend.entity.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.model.http.response.EventSheetTimeSlotResponse
import com.meetgom.backend.utils.extends.addTimeZone
import com.meetgom.backend.utils.extends.toLocalDateTimeWithTimeZone
import com.meetgom.backend.utils.extends.toTimeString
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

data class EventSheetTimeSlot(
    val eventSheetId: Long? = null,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
) : Comparable<EventSheetTimeSlot> {
    override fun compareTo(other: EventSheetTimeSlot): Int {
        return compareValuesBy(this, other, { it.startTime }, { it.endTime })
    }

    fun convertTimeZone(
        hostTimeZone: TimeZone,
        timeZone: TimeZone
    ): List<EventSheetTimeSlot> {
        val startDateTime = date.atTime(startTime).addTimeZone(hostTimeZone).toLocalDateTimeWithTimeZone(timeZone)
        val endDateTime =
            date.plusDays(if (endTime == LocalTime.MIN) 1 else 0).atTime(endTime).addTimeZone(hostTimeZone)
                .toLocalDateTimeWithTimeZone(timeZone)
        val dateRange = startDateTime
            .toLocalDate()
            .datesUntil(
                endDateTime.toLocalDate().plusDays(if (endDateTime.toLocalTime() != LocalTime.MIN) 1 else 0),
                Period.ofDays(1)
            )
            .toList()

        return dateRange
            .mapIndexed { idx, date ->
                println("EventSheetTimeSlot: $date")
                EventSheetTimeSlot(
                    eventSheetId = this.eventSheetId,
                    date = date,
                    startTime = if (idx == 0) startDateTime.toLocalTime() else LocalTime.of(0, 0),
                    endTime = if (idx == dateRange.size - 1) endDateTime.toLocalTime() else LocalTime.of(0, 0)
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

    fun toResponse(showDate: Boolean = true): EventSheetTimeSlotResponse {
        val dayOfWeek = date.dayOfWeek.name
        val startDateTimeString = startTime.toTimeString()
        var endDateTimeString = endTime.toTimeString()
        if (endDateTimeString == "00:00") {
            endDateTimeString = "24:00"
        }
        return EventSheetTimeSlotResponse(
            date = if (showDate) date else null,
            dayOfWeek = dayOfWeek,
            startDateTime = startDateTimeString,
            endDateTime = endDateTimeString
        )
    }
}
