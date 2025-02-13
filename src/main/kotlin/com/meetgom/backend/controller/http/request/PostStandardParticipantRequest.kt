package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema


@Schema(title = "Post Standard Participant Request")
data class PostStandardParticipantRequest(
    @Schema(
        title = "region",
        description = "timezone_region",
        defaultValue = "Asia/Seoul"
    )
    val region: String?,
    @Schema(
        title = "Available Time Slots",
        description = "사용자 가능 시간 구간",
        required = true
    )
    val availableTimeSlots: List<PostParticipantAvailableTimeSlotRequest>,
)