package com.meetgom.backend.exception.exceptions

import com.meetgom.backend.exception.common.MGExceptionStatus
import com.meetgom.backend.exception.common.MGExceptions

enum class ParticipantExceptions(
    override val status: MGExceptionStatus,
    override val message: String,
) : MGExceptions {
    AVAILABLE_TIME_SLOT_DATE_NOT_FOUND(MGExceptionStatus.BAD_REQUEST, "AvailableTimeSlot의 날짜를 찾을 수 없습니다."),
    AVAILABLE_TIME_SLOT_DAY_OF_WEEK_NOT_FOUND(MGExceptionStatus.BAD_REQUEST, "AvailableTimeSlot의 요일을 찾을 수 없습니다."),
    AVAILABLE_TIME_SLOT_INVALID_DAY_OF_WEEK(MGExceptionStatus.BAD_REQUEST, "잘못된 요일입니다."),
    AVAILABLE_TIME_SLOT_INVALID_TIME_SLOTS(MGExceptionStatus.BAD_REQUEST, "잘못된 시간 슬롯입니다."),
}