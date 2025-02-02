package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(title = "Post Event Sheet Time Slot Request")
data class PostParticipantAvailableTimeSlotRequest(
    @Schema(
        title = "Date",
        description = "이벤트 시트 특정 날짜 구간 날짜",
    )
    val date: LocalDate,

    @Schema(
        title = "Start Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "00:00"
    )
    val startTime: String,

    @Schema(
        title = "End Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "24:00"
    )
    val endTime: String,
)