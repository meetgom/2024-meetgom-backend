package com.meetgom.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/v1/events"])
class EventController {
    @GetMapping(path = ["/"])
    fun getEvent() {

    }
}