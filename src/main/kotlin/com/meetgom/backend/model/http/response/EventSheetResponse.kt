package com.meetgom.backend.model.http.response

import com.meetgom.backend.type.EventDateType
import java.util.*

data class EventSheetResponse(
    val id: Long?,
    val eventCode: String,
    val name: String,
    val description: String?,
    val eventDateType: EventDateType,
    val activeStartDate: Date?,
    val activeEndDate: Date?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val isActive: Boolean,
    val timeZone: TimeZoneResponse,
    val eventSheetTimeSlots: List<EventSheetTimeSlotResponse>
)