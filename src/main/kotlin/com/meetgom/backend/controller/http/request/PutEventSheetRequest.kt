package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class PutEventSheetRequest(
    @Schema(
        title = "Name",
        description = "이벤트 시트 이름",
        nullable = true
    )
    val name: String?,

    @Schema(
        title = "Description",
        description = "이벤트 시트 설명",
        nullable = true
    )
    val description: String?,

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
)