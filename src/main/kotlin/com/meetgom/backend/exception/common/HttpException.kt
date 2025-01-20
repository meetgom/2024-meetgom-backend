package com.meetgom.backend.exception.common

import org.springframework.http.HttpStatus

open class HttpException(
    val status: HttpStatus,
    message: String?,
) : Exception(message)