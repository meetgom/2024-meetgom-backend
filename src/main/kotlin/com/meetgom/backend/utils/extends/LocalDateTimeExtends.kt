package com.meetgom.backend.utils.extends

import com.meetgom.backend.domain.model.common.TimeZone
import com.meetgom.backend.utils.utils.TimeUtils
import java.time.*
import java.time.temporal.ChronoUnit

fun LocalDateTime.atTimeZone(timeZone: TimeZone): ZonedDateTime {
    return this.atZone(ZoneId.of(timeZone.region))
}

fun LocalDateTime.changeWithTimeZone(from: TimeZone, to: TimeZone): LocalDateTime {
    val zonedDateTime = this.atTimeZone(from)
    val isMax = zonedDateTime.toLocalTime() == TimeUtils.MAX_LOCAL_TIME
    val convertedZonedDateTime = zonedDateTime.toTimeZone(to)
    return if (isMax && convertedZonedDateTime.hour < 23) {
        convertedZonedDateTime.truncatedTo(ChronoUnit.MINUTES).plusMinutes(1).toLocalDateTime()
    } else {
        convertedZonedDateTime.toLocalDateTime()
    }
}

fun LocalDateTime.untilDays(endDateTime: LocalDateTime): List<LocalDate> {
    return this.toLocalDate().untilDays(endDate = endDateTime.toLocalDate().plusDays(1))
}