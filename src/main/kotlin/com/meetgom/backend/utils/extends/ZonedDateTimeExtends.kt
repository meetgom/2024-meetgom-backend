package com.meetgom.backend.utils.extends

import com.meetgom.backend.model.domain.TimeZone
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.toTimeZone(timeZone: TimeZone): ZonedDateTime {
    return this.withZoneSameInstant(ZoneId.of(timeZone.region))
}