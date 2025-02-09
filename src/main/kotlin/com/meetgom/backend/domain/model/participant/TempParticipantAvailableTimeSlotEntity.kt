package com.meetgom.backend.domain.model.participant

import com.meetgom.backend.controller.http.response.ParticipantAvailableTimeSlotResponse
import com.meetgom.backend.data.entity.participant.ParticipantAvailableTimeSlotEntity
import com.meetgom.backend.data.entity.participant.ParticipantAvailableTimeSlotPrimaryKey
import com.meetgom.backend.data.entity.participant.ParticipantEntity
import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.utils.TimeUtils
import com.meetgom.backend.utils.extends.changeWithTimeZone
import com.meetgom.backend.utils.extends.untilDays
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