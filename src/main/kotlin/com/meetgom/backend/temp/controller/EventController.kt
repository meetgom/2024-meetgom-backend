package com.meetgom.backend.temp.controller

import com.meetgom.backend.temp.model.http.response.EventCreateRequest
import com.meetgom.backend.temp.model.domain.EventDateType
import com.meetgom.backend.temp.model.domain.Weekday
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/v1/events")
class EventController(private val eventService: EventService) {

    init {

    }

    @PostMapping
    fun createEvent(@RequestBody eventCreateRequest: EventCreateRequest): EventEntity {
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