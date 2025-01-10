package com.meetgom.backend.model.http.request

import com.meetgom.backend.model.domain.EventSheet
import com.meetgom.backend.type.EventDateType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size
import java.util.*

@Schema(title = "Event Sheet Request", description = "POST/PUT")
data class EventSheetRequest(
    val name: String,
    @Schema(title = "Description", description = "이벤트 설명", defaultValue = "null")
    val description: String?,
    val eventDateType: EventDateType,
    @Schema(title = "Time Zone Id", description = "이벤트 시간대 ID", defaultValue = "1")
    val timeZoneId: Long,
    @Schema(title = "Event Sheet Time Slots", description = "이벤트 시간 슬롯")
    val eventSheetTimeSlots: List<EventSheetTimeSlotRequest>?,
    @Schema(title = "Pin Code", description = "이벤트 접속 핀코드", defaultValue = "null")
    val pinCode: Int?,
    @Schema(title = "Active Start Date", description = "이벤트 활성 시작 시간(미사용)", defaultValue = "null")
    val activeStartDate: Date?,
    @Schema(title = "Active End Date", description = "이벤트 활성 종료 시간(미사용)", defaultValue = "null")
    val activeEndDate: Date?,
    @Schema(title = "Is Active", description = "이벤트 활성 여부 (미사용)")
    val isActive: Boolean,
    @Size(min = 1, max = 100)
    @Schema(title = "Word Count", description = "이벤트 코드 단어 수", defaultValue = "3")
    val wordCount: Int = 3
)

