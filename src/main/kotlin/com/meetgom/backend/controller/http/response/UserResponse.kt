package com.meetgom.backend.controller.http.response

import com.meetgom.backend.type.UserType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User response")
class UserResponse(
    @Schema(description = "User name")
    val userName: String,
    @Schema(description = "User type")
    val userType: UserType
)