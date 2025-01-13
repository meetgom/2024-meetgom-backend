package com.meetgom.backend.model.http.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(title = "Post Event Sheet Time Slot Request")
data class PostEventSheetTimeSlotRequest(
    @Schema(
        title = "Start Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "2025-01-01T00:00"
    )
    val startDateTime: LocalDateTime,

    @Schema(
        title = "End Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "2025-01-02T00:00"
    )
    val endDateTime: LocalDateTime,
)