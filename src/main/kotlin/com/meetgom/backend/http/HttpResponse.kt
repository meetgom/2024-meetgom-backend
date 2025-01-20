package com.meetgom.backend.http

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

data class HttpResponse<T>(
    @Schema(
        title = "success",
        description = "성공 여부",
        defaultValue = "true"
    )
    val success: Boolean = true,

    @Schema(
        title = "data",
        description = "전달할 데이터 값",
        nullable = true,
    )
    val data: T? = null,
    @Schema(
        title = "message",
        description = "메세지",
        nullable = true,
        defaultValue = "String"
    )
    val message: String? = null
) {
    companion object {
        fun <T> of(response: T, message: String? = null): HttpResponse<T> = HttpResponse(
            data = response,
            message = message
        )

        fun <T> error(error: HttpException): HttpResponse<T> = HttpResponse(
            success = false,
            message = error.message
        )
    }
}