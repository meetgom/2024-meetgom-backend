package com.meetgom.backend.model.http.request

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(title = "Event Sheet Time Slot Request", description = "POST/PUT")
data class EventSheetTimeSlotRequest(
    val eventSheetId: Long? = null,
    val date: Date,
    val startTime: Int,
    val endTime: Int
)