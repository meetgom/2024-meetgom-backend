package com.meetgom.backend.utils.extends

import com.meetgom.backend.model.domain.TimeZone
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.toLocalDateTimeWithTimeZone(timeZone: TimeZone): LocalDateTime {
    return this.withZoneSameInstant(ZoneId.of(timeZone.region)).toLocalDateTime()
}