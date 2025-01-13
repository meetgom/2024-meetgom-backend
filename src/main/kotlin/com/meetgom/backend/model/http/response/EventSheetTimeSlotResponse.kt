package com.meetgom.backend.model.http.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class EventSheetTimeSlotResponse(
    @Schema(
        title = "Start Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "yyyy-MM-dd'T'hh:mm:ss'Z'",
    )
    val startDateTime: LocalDateTime,

    @Schema(
        title = "End Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "yyyy-MM-dd'T'hh:mm:ss'Z'",
    )
    val endDateTime: LocalDateTime
)