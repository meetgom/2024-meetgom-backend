package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema

data class PutParticipantRequest(
    @Schema(
        title = "User Name",
        description = "사용자명"
    )
    val userName: String?,

    @Schema(
        title = "region",
        description = "timezone_region",
        defaultValue = "Asia/Seoul"
    )
    val region: String?,

    @Schema(
        title = "Available Time Slots",
        description = "사용자 가능 시간 구간",
    )
    val availableTimeSlots: List<PostParticipantAvailableTimeSlotRequest>?,
)