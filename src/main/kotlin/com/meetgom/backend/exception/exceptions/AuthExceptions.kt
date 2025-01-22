package com.meetgom.backend.exception.exceptions

import com.meetgom.backend.exception.common.MGExceptionStatus
import com.meetgom.backend.exception.common.MGExceptions

enum class AuthExceptions(
    override val status: MGExceptionStatus,
    override val message: String,
) : MGExceptions {
    NOT_FOUND_USER(MGExceptionStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NEED_EMAIL_ARGUMENT(MGExceptionStatus.BAD_REQUEST, "이메일이 필요합니다."),
    NEED_PASSWORD_ARGUMENT(MGExceptionStatus.BAD_REQUEST, "비밀번호가 필요합니다."),
    EMAIL_ALREADY_EXISTS(MGExceptionStatus.BAD_REQUEST, "이미 가입된 이메일입니다.")
}