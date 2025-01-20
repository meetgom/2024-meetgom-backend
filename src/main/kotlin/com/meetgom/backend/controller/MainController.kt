package com.meetgom.backend.controller

import com.meetgom.backend.model.http.HttpResponse
import com.meetgom.backend.model.http.response.ServerStatusResponse
import com.meetgom.backend.utils.listener.ApplicationStartedEventListener
import com.meetgom.backend.utils.GitUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "main", description = "")
@RestController
@RequestMapping(path = ["/"])
class MainController {
    @GetMapping(path = ["/status"])
    @Operation(summary = "Server Status")
    fun getMeetgomStatus(): HttpResponse<ServerStatusResponse> {
        val running = "Server is running"
        val startedAt = ApplicationStartedEventListener.startTime
        val gitLatestLog = kotlin
            .runCatching { GitUtils.getGitLogs().first() }
            .getOrNull()
        return HttpResponse.of(ServerStatusResponse(running, startedAt, gitLatestLog))
    }
}