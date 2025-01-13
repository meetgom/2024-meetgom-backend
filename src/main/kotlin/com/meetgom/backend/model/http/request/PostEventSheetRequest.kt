package com.meetgom.backend.model.http.request

import com.meetgom.backend.type.EventDateType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(title = "Post Event Sheet Request", description = "POST")
data class PostEventSheetRequest(
    @Schema(
        title = "Name",
        description = "이벤트 시트 이름",
        required = true
    )
    val name: String,

    @Schema(title = "Description", description = "이벤트 시트 설명")
    val description: String?,

    @Schema(
        title = "Event Date Type",
        description = "이벤트 시트 시간 타입 (SPECIFIC_DATES | RECURRING_WEEKDAYS)",
        required = true
    )
    val eventDateType: EventDateType,

    @Schema(
        title = "Time Zone Region",
        description = "이벤트 시트 시간대",
        defaultValue = "Asia/Seoul"
    )
    val timeZone: String,

    @Schema(title = "Event Sheet Time Slots", description = "이벤트 시트 시간 슬롯 목록")
    val eventSheetTimeSlots: List<PostEventSheetTimeSlotRequest>?,

    @Schema(
        title = "Pin Code",
        description = "이벤트 시트 접속 핀코드",
        nullable = true,
    )
    val pinCode: String? = null,

    @Schema(
        title = "Active Start Date",
        description = "이벤트 시트 활성 시작 시간(미사용)",
        nullable = true,
        defaultValue = "2025-01-01T00:00"
    )
    val activeStartDateTime: LocalDateTime?,

    @Schema(
        title = "Active End Date",
        description = "이벤트 시트 활성 종료 시간(미사용)",
        nullable = true,
        defaultValue = "2099-12-31T00:00"
    )
    val activeEndDateTime: LocalDateTime?,

    @Schema(
        title = "Manual Active",
        description = "이벤트 시트 수동 활성 여부 (미사용)",
        nullable = true,
        defaultValue = "true",
    )
    val manualActive: Boolean?,

    @Size(min = 1, max = 100)
    @Schema(
        title = "Word Count",
        description = "이벤트 시트 코드 단어 수",
        nullable = true,
        defaultValue = "3",
        hidden = true
    )
    val wordCount: Int?
)

