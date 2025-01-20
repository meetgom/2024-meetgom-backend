package com.meetgom.backend.http

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class HttpExceptionHandler {
    @ExceptionHandler(Exception::class)
    final fun handleHttpException(exception: Exception): ResponseEntity<Any> {
        val httpResponse = HttpResponse.error(exception)
        return ResponseEntity(httpResponse, (exception as? HttpException)?.status ?: HttpStatus.INTERNAL_SERVER_ERROR)
    }
}