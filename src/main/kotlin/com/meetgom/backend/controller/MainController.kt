package com.meetgom.backend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "main", description = "")
@RestController
@RequestMapping(path = ["/"])
class MainController {
    @GetMapping(path = ["/status"])
    fun getMeetgomStatus(): String {
        return "server is running!"
    }
}
