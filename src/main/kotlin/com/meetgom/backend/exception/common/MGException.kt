package com.meetgom.backend.exception.common

import org.springframework.http.HttpStatus

enum class MGExceptionStatus(val httpStatus: HttpStatus) {
    NOT_FOUND(HttpStatus.NOT_FOUND),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    UNCAUGHT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY),
}

open class MGException(
    status: MGExceptionStatus,
    message: String?,
) : HttpException(status.httpStatus, message)

interface MGExceptions {
    val status: MGExceptionStatus
    val message: String
    fun toException(): MGException {
        return MGException(status, message)
    }
}