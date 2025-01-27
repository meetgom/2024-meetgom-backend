package com.meetgom.backend.controller.http.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "Post StandardUser Request")
data class PostStandardUserRequest(
    val displayName: String,
    val email: String,
    val password: String,
)