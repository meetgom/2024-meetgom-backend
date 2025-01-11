package com.meetgom.backend.model.http.response

import com.meetgom.backend.type.EventDateType
import java.time.LocalDateTime

data class EventSheetResponse(
    val id: Long?,
    val eventCode: String,
    val name: String,
    val description: String?,
    val eventDateType: EventDateType,
    val activeStartDateTime: LocalDateTime?,
    val activeEndDateTime: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val isActive: Boolean,
    val timeZone: String,
    val eventSheetTimeSlots: List<EventSheetTimeSlotResponse>
)