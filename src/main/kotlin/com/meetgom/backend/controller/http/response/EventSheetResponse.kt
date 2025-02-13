package com.meetgom.backend.controller.http.response

import com.meetgom.backend.type.EventSheetType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class EventSheetResponse(
    @Schema(
        title = "ID",
        description = "이벤트 시트 ID",
        defaultValue = "id",
    )
    val id: Long?,

    @Schema(
        title = "Event Code",
        description = "이벤트 시트 코드",
        defaultValue = "word-word-word",
    )
    val eventSheetCode: String,

    @Schema(
        title = "Name",
        description = "이벤트 시트 명",
        defaultValue = "name",
    )
    val name: String,

    @Schema(
        title = "Description",
        description = "이벤트 시트 설명",
        defaultValue = "description",
    )
    val description: String?,

    @Schema(
        title = "Event Date Type",
        description = "이벤트 시트 시간 타입",
        defaultValue = "SPECIFIC_DATES | RECURRING_WEEKDAYS",
    )
    val eventSheetType: EventSheetType,

    @Schema(
        title = "Active Start Date",
        description = "이벤트 시트 활성 시작 시간",
        defaultValue = "yyyy-MM-dd'T'hh:mm:ss",
    )
    val activeStartDateTime: LocalDateTime?,

    @Schema(
        title = "Active End Date",
        description = "이벤트 시트 활성 종료 시간",
        defaultValue = "yyyy-MM-dd'T'hh:mm:ss",
    )
    val activeEndDateTime: LocalDateTime?,

    @Schema(
        title = "Manual Active",
        description = "이벤트 시트 수동 활성",
        defaultValue = "booelan?",
    )
    val manualActive: Boolean?,

    @Schema(
        title = "Time Zone",
        description = "이벤트 시트 시간대",
        defaultValue = "Asia/Seoul",
    )
    val timeZone: String,

    @Schema(
        title = "Time Zone",
        description = "호스트가 설정한 이벤트 시트 시간대",
        defaultValue = "Asia/Seoul",
    )
    val hostTimeZone: String,

    @Schema(
        title = "Event Sheet Time Slots",
        description = "이벤트 시트 시간 슬롯 목록",
    )
    val eventSheetTimeSlots: List<EventSheetTimeSlotResponse>,

    @Schema(
        title = "Is Active",
        description = "이벤트 시트 활성 여부",
        defaultValue = "true",
    )
    val isActive: Boolean,

    @Schema(
        title = "Created At",
        description = "이벤트 시트 생성 시각",
        defaultValue = "yyyy-MM-dd'T'hh:mm:ss'Z'",
    )
    val createdAt: ZonedDateTime?,

    @Schema(
        title = "Updated At",
        description = "이벤트 시트 업데이트 시각",
        defaultValue = "yyyy-MM-dd'T'hh:mm:ss'Z'",
    )
    val updatedAt: ZonedDateTime?,

    @Schema(
        title = "Participant Time Slot Table",
        description = "전체 참여자 시간 슬롯 테이블",
    )
    val timeSlotTable: List<ParticipantTimeSlotTableResponse>,

    @Schema(
        title = "Participant",
        description = "EventSheet 참여자",
    )
    val participant: List<ParticipantResponse>
)