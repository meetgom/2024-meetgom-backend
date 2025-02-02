package com.meetgom.backend.utils.extends

import com.meetgom.backend.domain.model.common.TimeZone
import java.time.*

fun LocalDateTime.atTimeZone(timeZone: TimeZone): ZonedDateTime {
    return this.atZone(ZoneId.of(timeZone.region))
}

fun LocalDateTime.changeWithTimeZone(from: TimeZone, to: TimeZone): LocalDateTime {
    return this.atTimeZone(from)
        .toTimeZone(to)
        .toLocalDateTime()
}

fun LocalDateTime.untilDays(endDateTime: LocalDateTime): List<LocalDate> {
    return this.toLocalDate().untilDays(endDate = endDateTime.toLocalDate().plusDays(1))
}