package com.meetgom.backend.temp.model.http.response

data class EventCreateRequest(
    val name: String,
    val timeZone: String,
    val eventDateType: String,
    val specificDates: List<String>?,
    val recurringWeekdays: List<String>?,
    val startTime: Int,
    val endTime: Int
)