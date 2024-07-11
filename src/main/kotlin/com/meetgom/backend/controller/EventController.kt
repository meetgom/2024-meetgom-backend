package com.meetgom.backend.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller("/events")
class EventController {
    @GetMapping
    fun getEvents(): String {
        return "events"
    }
}