package com.meetgom.backend.temp.service

import com.meetgom.backend.temp.model.domain.EventDateType
import com.meetgom.backend.temp.model.domain.Weekday
import com.meetgom.backend.util.encodeDaysToByte
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class EventService(private val eventRepository: EventRepository) {
    fun getEvents(): String {
        return eventRepository.findAll().toString()
    }

    fun createEvent(
        name: String,
        timeZone: String,
        eventDateType: EventDateType,
        specificDates: List<LocalDate>?,
        recurringWeekdays: List<Weekday>?,
        startTime: Int,
        endTime: Int
    ): EventEntity {
        if (eventDateType == EventDateType.SPECIFIC_DATES && specificDates.isNullOrEmpty()) {
            throw IllegalArgumentException("specificDates must be provided for EventDateType.SPECIFIC_DATES")
        }
        if (eventDateType == EventDateType.RECURRING_WEEKDAYS && recurringWeekdays.isNullOrEmpty()) {
            throw IllegalArgumentException("recurringWeekdays must be provided for EventDateType.RECURRING_WEEKDAYS")
        }

        val event = EventEntity(
            timeZone = timeZone,
            title = name,
            eventDateType = eventDateType,
            startTime = startTime,
            endTime = endTime
        )

        if (eventDateType == EventDateType.SPECIFIC_DATES) {
            specificDates?.forEach { date ->
                event.availableDates.add(EventAvailableDateEntity(event = event, date = date))
            }
        } else {
            event.availableDaysOfWeek = EventAvailableDayOfWeekEntity(
                event = event,
                dayOfWeek = encodeDaysToByte(recurringWeekdays!!),
            )
        }

        return eventRepository.save(event)
    }
}