package com.meetgom.backend.exception.exceptions

import com.meetgom.backend.exception.common.MGExceptionStatus
import com.meetgom.backend.exception.common.MGExceptions

enum class EventSheetExceptions(
    override val status: MGExceptionStatus,
    override val message: String,
) : MGExceptions {
    EVENT_SHEET_NOT_ACTIVE(MGExceptionStatus.BAD_REQUEST, "이벤트 시트가 활성화되지 않았습니다."),
    EVENT_SHEET_CODE_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "EventSheet 코드를 찾을 수 없습니다."),
    EVENT_SHEET_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "EventSheet을 찾을 수 없습니다."),
    TIME_ZONE_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "TimeZone을 찾을 수 없습니다."),
    PARTICIPANT_ROLE_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "참가자 역할을 찾을 수 없습니다."),
    USER_NOT_FOUND(MGExceptionStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    MAX_EVENT_SHEET_CODES_REACHED(MGExceptionStatus.UNPROCESSABLE_ENTITY, "최대 이벤트 코드 개수를 초과하였습니다."),
    INVALID_EVENT_SHEET_TIME_SLOTS(MGExceptionStatus.BAD_REQUEST, "잘못된 시간 슬롯 형식입니다."),
    INVALID_DAY_OF_WEEKS(MGExceptionStatus.BAD_REQUEST, "잘못된 요일 형식입니다."),
    UNMATCHED_EVENT_SHEET_CODE(MGExceptionStatus.BAD_REQUEST, "이벤트 코드가 일치하지 않습니다."),
    UNMATCHED_EVENT_SHEET(MGExceptionStatus.BAD_REQUEST, "이벤트 시트가 일치하지 않습니다."),
    UNMATCHED_ROLE_TYPE(MGExceptionStatus.BAD_REQUEST, "역할 타입이 일치하지 않습니다."),
}