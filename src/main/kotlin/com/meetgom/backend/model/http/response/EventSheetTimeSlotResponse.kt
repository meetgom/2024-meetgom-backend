package com.meetgom.backend.model.http.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class EventSheetTimeSlotResponse(
    @Schema(
        title = "Date",
        description = "이벤트 시트 시간 슬롯 날짜",
        defaultValue = "yyyy-MM-dd",
    )
    var date: LocalDate,
    @Schema(
        title = "startTime",
        description = "이벤트 시트 시간 슬롯 시작 시간",
        defaultValue = "hh:mm",
    )
    var startTime: String,
    @Schema(
        title = "startTime",
        description = "이벤트 시트 시간 슬롯 종료 시간",
        defaultValue = "hh:mm",
    )
    var endTime: String
)