package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Post Anonymous Participant Request")
data class PostAnonymousParticipantRequest(
    val userName: String,
    val password: String,
    val region: String,
    val availableTimeSlots: List<PostParticipantAvailableTimeSlotRequest>,
)