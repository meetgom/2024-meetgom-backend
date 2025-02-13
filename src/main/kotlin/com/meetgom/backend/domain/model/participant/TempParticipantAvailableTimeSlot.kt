package com.meetgom.backend.domain.model.participant

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class TempParticipantAvailableTimeSlot(
    val participantId: Long? = null,
    val date: LocalDate?,
    val dayOfWeek: DayOfWeek?,
    val startTime: LocalTime,
    val endTime: LocalTime
)