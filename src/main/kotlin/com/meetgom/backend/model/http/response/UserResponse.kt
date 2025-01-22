package com.meetgom.backend.model.http.response

import com.meetgom.backend.type.UserType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User response")
class UserResponse(
    @Schema(description = "Display name")
    val displayName: String,
    @Schema(description = "User type")
    val userType: UserType
)