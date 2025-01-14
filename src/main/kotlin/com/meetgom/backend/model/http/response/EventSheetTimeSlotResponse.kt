package com.meetgom.backend.model.http.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class EventSheetTimeSlotResponse(
    @Schema(title = "Date",
        description = "이벤트 시트 특정 날짜 구간 날짜",
        defaultValue = "yyyy-MM-dd",
    )
    val date: LocalDate?,

    @Schema(
        title = "Start Date Time",
        description = "이벤트 시트 특정 날짜 구간 시작 시간",
        defaultValue = "MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY | SUNDAY",
    )
    val dayOfWeek: String,

    @Schema(
        title = "Start Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "hh:mm",
    )
    val startDateTime: String,

    @Schema(
        title = "End Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "hh:mm",
    )
    val endDateTime: String
)