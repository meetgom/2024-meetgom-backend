package com.meetgom.backend.http

class HttpException(
    status: HttpStatus,
    message: String?,
) : Exception(
    "${status.status}: $message"
) {

}