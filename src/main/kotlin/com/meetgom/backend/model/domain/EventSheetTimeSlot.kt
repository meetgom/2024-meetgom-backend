package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.entity.EventSheetTimeSlotEntity
import com.meetgom.backend.entity.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.model.http.response.EventSheetTimeSlotResponse
import com.meetgom.backend.utils.extends.addTimeZone
import com.meetgom.backend.utils.extends.toLocalDateTimeWithTimeZone
import java.time.LocalDateTime

data class EventSheetTimeSlot(
    val eventSheetId: Long? = null,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) : Comparable<EventSheetTimeSlot> {
    override fun compareTo(other: EventSheetTimeSlot): Int {
        return compareValuesBy(this, other, { it.startDateTime }, { it.endDateTime })
    }

    fun convertTimeZone(
        hostTimeZone: TimeZone,
        timeZone: TimeZone
    ): EventSheetTimeSlot {
        return EventSheetTimeSlot(
            eventSheetId = this.eventSheetId,
            startDateTime = this.startDateTime.addTimeZone(hostTimeZone).toLocalDateTimeWithTimeZone(timeZone),
            endDateTime = this.endDateTime.addTimeZone(hostTimeZone).toLocalDateTimeWithTimeZone(timeZone)
        )
    }

    // MARK: - Converters
    fun toEntity(eventSheetEntity: EventSheetEntity? = null): EventSheetTimeSlotEntity {
        return EventSheetTimeSlotEntity(
            eventSheetTimeSlotPrimaryKey = EventSheetTimeSlotPrimaryKey(
                eventSheetId = this.eventSheetId,
                startDateTime = this.startDateTime
            ),
            endDateTime = this.endDateTime,
            eventSheetEntity = eventSheetEntity
        )
    }

    fun toResponse(): EventSheetTimeSlotResponse {
        return EventSheetTimeSlotResponse(
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )
    }
}