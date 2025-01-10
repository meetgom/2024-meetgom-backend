package com.meetgom.backend.model.domain

import com.meetgom.backend.entity.EventSheetEntity
import com.meetgom.backend.entity.EventSheetTimeSlotEntity
import com.meetgom.backend.entity.EventSheetTimeSlotPrimaryKey
import com.meetgom.backend.model.http.response.EventSheetTimeSlotResponse
import java.util.Date

data class EventSheetTimeSlot(
    val eventSheetId: Long? = null,
    val date: Date,
    val startTime: Int,
    val endTime: Int
) {
    fun toEntity(eventSheetEntity: EventSheetEntity? = null): EventSheetTimeSlotEntity {
        return EventSheetTimeSlotEntity(
            eventSheetTimeSlotPrimaryKey = EventSheetTimeSlotPrimaryKey(
                eventSheetId = this.eventSheetId,
                date = this.date
            ),
            startTime = this.startTime,
            endTime = this.endTime,
            eventSheetEntity = eventSheetEntity
        )
    }

    fun toResponse(): EventSheetTimeSlotResponse {
        return EventSheetTimeSlotResponse(
            date = this.date,
            startTime = this.startTime,
            endTime = this.endTime
        )
    }
}