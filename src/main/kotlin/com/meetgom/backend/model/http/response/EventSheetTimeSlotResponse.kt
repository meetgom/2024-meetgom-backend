package com.meetgom.backend.model.http.response

import java.time.LocalDate

class EventSheetTimeSlotResponse(
    var date: LocalDate,
    var startTime: String,
    var endTime: String
)