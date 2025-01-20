package com.meetgom.backend.http

import org.springframework.http.HttpStatus

open class HttpException(
    val status: HttpStatus,
    message: String?,
) : Exception(message)