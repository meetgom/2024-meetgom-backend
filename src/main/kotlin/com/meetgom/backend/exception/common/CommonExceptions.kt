package com.meetgom.backend.exception.common

import com.meetgom.backend.exception.common.MGExceptionStatus
import com.meetgom.backend.exception.common.MGExceptions

enum class CommonExceptions(
    override val status: MGExceptionStatus,
    override val message: String
) : MGExceptions {
    INVALID_TIME_FORMAT(MGExceptionStatus.BAD_REQUEST, "잘못된 시간 형식입니다. (HH:mm)");
}