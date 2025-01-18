package com.meetgom.backend.utils.extends

import java.time.LocalDate
import java.time.Period

fun LocalDate.untilDays(endDate: LocalDate): List<LocalDate> {
    return this.datesUntil(
        endDate,
        Period.ofDays(1)
    )
        .toList()
}