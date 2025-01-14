package com.meetgom.backend.utils.extends

import com.meetgom.backend.utils.TimeUtils
import java.time.LocalTime

fun LocalTime.toTimeString(): String {
    return this.format(java.time.format.DateTimeFormatter.ofPattern(TimeUtils.BASE_TIME_FORMAT))
}