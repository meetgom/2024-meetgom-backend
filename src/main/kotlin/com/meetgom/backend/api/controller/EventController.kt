package com.meetgom.backend.api.controller

import com.meetgom.backend.api.request.EventCreateRequest
import com.meetgom.backend.entity.EventEntity
import com.meetgom.backend.model.EventDateType
import com.meetgom.backend.model.Weekday
import com.meetgom.backend.service.EventService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/v1/events")
class EventController(private val eventService: EventService) {
    @PostMapping
    fun createEvent(@RequestBody eventCreateRequest: EventCreateRequest) : EventEntity {
        return eventService.createEvent(
            name = eventCreateRequest.name,
            timeZone = eventCreateRequest.timeZone,
            eventDateType = EventDateType.valueOf(eventCreateRequest.eventDateType),
            specificDates = eventCreateRequest.specificDates?.map {
                LocalDate.parse(it)
            },
            recurringWeekdays = eventCreateRequest.recurringWeekdays?.map {
                Weekday.valueOf(it)
            },
            startTime = eventCreateRequest.startTime,
            endTime = eventCreateRequest.endTime
        )
    }
}