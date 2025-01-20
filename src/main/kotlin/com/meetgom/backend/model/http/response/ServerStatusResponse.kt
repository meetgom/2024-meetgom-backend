package com.meetgom.backend.model.http.response

import java.time.ZonedDateTime

data class ServerStatusResponse(
    val status: String,
    val startedAt: ZonedDateTime?,
    val latestGitLog: String?,
)