package com.meetgom.backend.exception.common

import com.meetgom.backend.controller.http.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(HttpException::class)
    final fun handleHttpException(exception: HttpException): ResponseEntity<Any> {
        val httpResponse = HttpResponse.error(exception)
        return ResponseEntity(httpResponse, (exception as? HttpException)?.status ?: HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    final fun handleGlobalException(exception: Exception): ResponseEntity<Any> {
        val httpResponse = HttpResponse.error(exception)
        return ResponseEntity(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}