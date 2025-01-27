package com.meetgom.backend.controller.http

import com.meetgom.backend.global.exception.common.HttpException
import io.swagger.v3.oas.annotations.media.Schema

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
        title = "error",
        description = "에러",
        nullable = true,
    )
    val error: String? = null,

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

        fun error(error: Exception): HttpResponse<Any> = HttpResponse(
            success = false,
            error = (error as? HttpException)?.status?.name ?: "UNCAUGHT_ERROR",
            message = error.message
        )
    }
}