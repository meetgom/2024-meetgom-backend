package com.meetgom.backend.controller.http.response

import com.meetgom.backend.utils.utils.model.GitInfo
import java.time.ZonedDateTime

data class ServerStatusResponse(
    val status: String,
    val startedAt: ZonedDateTime?,
    val git: GitInfo?,
)