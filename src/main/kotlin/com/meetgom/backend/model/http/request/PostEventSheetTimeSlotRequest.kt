package com.meetgom.backend.model.http.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Schema(title = "Post Event Sheet Time Slot Request")
data class PostEventSheetTimeSlotRequest(
    @Schema(
        title = "Date",
        description = "이벤트 시트 날짜",
        defaultValue = "2025-01-01"
    )
    @Pattern(
        regexp = "^\\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$",
        message = "yyyy-MM-dd 형식으로 입력해주세요."
    )
    val date: LocalDate,

    @Schema(
        title = "Start Time",
        description = "이벤트 시트 특정 날짜 구간 시작 시간",
        defaultValue = "00:00"
    )
    @Pattern(
        regexp = "^((0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9])|(24:00)$",
        message = "hh:mm 형식으로 입력해주세요."
    )
    val startTime: String,

    @Schema(
        title = "End Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "24:00"
    )
    @Pattern(
        regexp = "^((0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9])|(24:00)$",
        message = "hh:mm 형식으로 입력해주세요."
    )
    val endTime: String
)