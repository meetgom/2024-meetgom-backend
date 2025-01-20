package com.meetgom.backend.exception.event_sheet

import com.meetgom.backend.exception.common.MGExceptionStatus
import com.meetgom.backend.exception.common.MGExceptions

enum class EventSheetExceptions(
    override val status: MGExceptionStatus,
    override val message: String,
) : MGExceptions {
    EVENT_SHEET_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "EventSheet을 찾을 수 없습니다."),
    TIME_ZONE_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "TimeZone을 찾을 수 없습니다."),
    MAX_EVENT_CODES_REACHED(MGExceptionStatus.UNPROCESSABLE_ENTITY, "최대 이벤트 코드 개수를 초과하였습니다."),
    INVALID_EVENT_SHEET_TIME_SLOTS(MGExceptionStatus.BAD_REQUEST, "잘못된 시간 슬롯 형식입니다."),
    INVALID_DAY_OF_WEEKS(MGExceptionStatus.BAD_REQUEST, "잘못된 요일 형식입니다."),
}