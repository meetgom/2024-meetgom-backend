package com.meetgom.backend.controller.http.response

import com.meetgom.backend.type.ParticipantRoleType

data class ParticipantResponse(
    val eventSheetCode: String,
    val user: UserResponse,
    val role: ParticipantRoleType,
    val timeZone: String,
    val availableTimeSlots: List<ParticipantAvailableTimeSlotResponse>
)