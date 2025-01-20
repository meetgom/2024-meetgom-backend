package com.meetgom.backend.model.http.request

import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.TimeUtils
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(title = "Post Event Sheet Request")
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
    val eventSheetType: EventSheetType,

    @Schema(
        title = "Time Zone Region",
        description = "이벤트 시트 시간대",
        defaultValue = "Asia/Seoul"
    )
    val timeZone: String,

    @Schema(
        title = "Pin Code",
        description = "이벤트 시트 접속 핀코드",
        nullable = true,
    )
    val pinCode: String,

    @Schema(
        title = "Active Start Date",
        description = "이벤트 시트 활성 시작 시간(옵션)",
        nullable = true,
        defaultValue = "2025-01-01T00:00"
    )
    val activeStartDateTime: LocalDateTime?,

    @Schema(
        title = "Active End Date",
        description = "이벤트 시트 활성 종료 시간(옵션)",
        nullable = true,
        defaultValue = "2099-12-31T00:00"
    )
    val activeEndDateTime: LocalDateTime?,

    @Schema(
        title = "Manual Active",
        description = "이벤트 시트 수동 활성 여부(옵션)",
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
    val wordCount: Int?,

    // MARK: - Event Sheet Time Slots
    @Schema(title = "Event Sheet Time Slots", description = "이벤트 시트 시간 슬롯 목록")
    val eventSheetTimeSlots: List<PostEventSheetTimeSlotRequest>,
)

@Schema(title = "Post Specific Dates Event Sheet Request")
data class PostSpecificDatesEventSheetRequest(
    @Schema(
        title = "Name",
        description = "이벤트 시트 이름",
        required = true
    )
    val name: String,

    @Schema(title = "Description", description = "이벤트 시트 설명")
    val description: String?,

    @Schema(
        title = "Time Zone Region",
        description = "이벤트 시트 시간대",
        defaultValue = "Asia/Seoul"
    )
    val timeZone: String,

    @Schema(
        title = "Pin Code",
        description = "이벤트 시트 접속 핀코드",
        nullable = true,
    )
    val pinCode: String,

    @Schema(
        title = "Active Start Date",
        description = "이벤트 시트 활성 시작 시간(옵션)",
        nullable = true,
        defaultValue = "2025-01-01T00:00"
    )
    val activeStartDateTime: LocalDateTime?,

    @Schema(
        title = "Active End Date",
        description = "이벤트 시트 활성 종료 시간(옵션)",
        nullable = true,
        defaultValue = "2099-12-31T00:00"
    )
    val activeEndDateTime: LocalDateTime?,

    @Schema(
        title = "Manual Active",
        description = "이벤트 시트 수동 활성 여부(옵션)",
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
    val wordCount: Int?,

    // MARK: - Event Sheet Time Slots를 위한 Specific Dates 설정
    @Schema(
        title = "Specific Dates",
        description = "이벤트 시트 특정 날짜 목록(range 시 각 날짜로 만들어서 보내주세요)"
    )
    val specificDates: List<LocalDate>,

    @Schema(
        title = "Specific Date's Start Time",
        description = "이벤트 시트 특정 날짜들의 시작 시간",
        defaultValue = "00:00"
    )
    @Pattern(
        regexp = "^(([01]?[0-9]|2[0-3]):[0-5][0-9])|(24:00)$",
        message = "시간 형식이 올바르지 않습니다."
    )
    val startTime: String,

    @Schema(
        title = "Specific Date's End Time",
        description = "이벤트 시트 특정 날짜들의 종료 시간"
    )
    @Pattern(
        regexp = "^(([01]?[0-9]|2[0-3]):[0-5][0-9])|(24:00)$",
        message = "시간 형식이 올바르지 않습니다."
    )
    val endTime: String,
) {
    fun toPostEventSheetRequest(): PostEventSheetRequest {
        val eventSheetTimeSlotsRequest = specificDates.map { date ->
            PostEventSheetTimeSlotRequest(
                date = date,
                startTime = startTime,
                endTime = endTime
            )
        }
        return PostEventSheetRequest(
            name = name,
            description = description,
            eventSheetType = EventSheetType.SPECIFIC_DATES,
            timeZone = timeZone,
            activeStartDateTime = activeStartDateTime,
            activeEndDateTime = activeEndDateTime,
            manualActive = manualActive,
            eventSheetTimeSlots = eventSheetTimeSlotsRequest,
            wordCount = wordCount ?: 3,
            pinCode = pinCode
        )
    }
}


@Schema(title = "Post Recurring Weekdays Event Sheet Request")
data class PostRecurringWeekdaysEventSheetRequest(
    @Schema(
        title = "Name",
        description = "이벤트 시트 이름",
        required = true
    )
    val name: String,

    @Schema(title = "Description", description = "이벤트 시트 설명")
    val description: String?,

    @Schema(
        title = "Time Zone Region",
        description = "이벤트 시트 시간대",
        defaultValue = "Asia/Seoul"
    )
    val timeZone: String,

    @Schema(
        title = "Pin Code",
        description = "이벤트 시트 접속 핀코드",
        nullable = true,
    )
    val pinCode: String,

    @Schema(
        title = "Active Start Date",
        description = "이벤트 시트 활성 시작 시간(옵션)",
        nullable = true,
        defaultValue = "2025-01-01T00:00"
    )
    val activeStartDateTime: LocalDateTime?,

    @Schema(
        title = "Active End Date",
        description = "이벤트 시트 활성 종료 시간(옵션)",
        nullable = true,
        defaultValue = "2099-12-31T00:00"
    )
    val activeEndDateTime: LocalDateTime?,

    @Schema(
        title = "Manual Active",
        description = "이벤트 시트 수동 활성 여부(옵션)",
        nullable = true,
        defaultValue = "true",
    )
    val manualActive: Boolean?,

    @Schema(
        title = "Word Count",
        description = "이벤트 시트 코드 단어 수",
        nullable = true,
        defaultValue = "3",
        hidden = true
    )
    @Size(min = 1, max = 100)
    val wordCount: Int?,

    // MARK: - Event Sheet Time Slots를 위한 Recurring Weekdays 설정
    @Schema(
        title = "Recurring Weekdays",
        description = "이벤트 시트 주기적인 요일 목록",
        defaultValue = "[\"MONDAY\", \"TUESDAY\", \"WEDNESDAY\", \"THURSDAY\", \"FRIDAY\", \"SATURDAY\", \"SUNDAY\"]"
    )
    val recurringWeekdays: List<DayOfWeek>,

    @Schema(
        title = "Recurring Weekday's Start Time",
        description = "이벤트 시트 주기적인 요일들의 시작 시간",
        defaultValue = "00:00"
    )
    @Pattern(
        regexp = "^(([01]?[0-9]|2[0-3]):[0-5][0-9])|(24:00)$",
        message = "시간 형식이 올바르지 않습니다."
    )
    val startTime: String,

    @Schema(
        title = "Recurring Weekday's End Time",
        description = "이벤트 시트 주기적인 요일들의 종료 시간",
        defaultValue = "24:00"
    )
    @Pattern(
        regexp = "^(([01]?[0-9]|2[0-3]):[0-5][0-9])|(24:00)$",
        message = "시간 형식이 올바르지 않습니다."
    )
    val endTime: String,
) {
    fun toPostEventSheetRequest(): PostEventSheetRequest {
        val eventSheetTimeSlotsRequest = this.recurringWeekdays.map {
            val date = TimeUtils.getClosestDayOfWeek(it)
            PostEventSheetTimeSlotRequest(
                date = date,
                startTime = this.startTime,
                endTime = this.endTime
            )
        }
        return PostEventSheetRequest(
            name = this.name,
            description = this.description,
            eventSheetType = EventSheetType.RECURRING_WEEKDAYS,
            timeZone = this.timeZone,
            activeStartDateTime = this.activeStartDateTime,
            activeEndDateTime = this.activeEndDateTime,
            manualActive = this.manualActive,
            eventSheetTimeSlots = eventSheetTimeSlotsRequest,
            wordCount = this.wordCount,
            pinCode = pinCode
        )
    }
}

