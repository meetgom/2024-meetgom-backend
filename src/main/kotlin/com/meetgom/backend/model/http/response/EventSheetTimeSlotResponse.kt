package com.meetgom.backend.model.http.response

import java.util.*

class EventSheetTimeSlotResponse(
    var date: Date,
    var startTime: Int,
    var endTime: Int
)