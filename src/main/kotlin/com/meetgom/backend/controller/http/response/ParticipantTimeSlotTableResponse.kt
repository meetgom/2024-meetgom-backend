package com.meetgom.backend.controller.http.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class ParticipantTimeSlotTableResponse(
    @Schema(
        title = "Date",
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
        defaultValue = "HH:mm",
    )
    val startTime: String,

    @Schema(
        title = "End Date Time",
        description = "이벤트 시트 특정 날짜 구간 종료 시간",
        defaultValue = "HH:mm",
    )
    val endTime: String,

    @Schema(
        title = "Available Participants Ratio",
        description = "가능한 인원 비율",
        defaultValue = "70.0",
    )
    val availableParticipantsRatio: Double,

    @Schema(
        title = "Available Participants Count",
        description = "참여 가능 인원 수",
        defaultValue = "70.0",
    )
    val availableParticipantsCount: Int,

    @Schema(
        title = "Participants",
        description = "참여자 목록",
        defaultValue = "[\"Participant1\", \"Participant2\", \"Participant3\"]",
    )
    val participants: List<String>
)