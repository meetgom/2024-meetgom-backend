package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Post Anonymous Participant Request")
data class PostAnonymousParticipantRequest(
    @Schema(
        title = "User Name",
        description = "사용자 이름",
        required = true,
        defaultValue = "anonymous_user"
    )
    val userName: String,
    @Schema(
        title = "password",
        description = "비밀번호",
        required = true,
        defaultValue = "password"
    )
    val password: String,

    @Schema(
        title = "confirmPassword",
        description = "비밀번호 확인",
        required = true,
        defaultValue = "confirmPassword"
    )
    val confirmPassword: String,

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
) {
    fun passwordMatch(): Boolean {
        return password == confirmPassword
    }
}

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