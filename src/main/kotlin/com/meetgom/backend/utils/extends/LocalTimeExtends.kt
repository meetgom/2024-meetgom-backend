package com.meetgom.backend.utils.extends

import java.time.LocalTime

fun LocalTime.max(vararg time: LocalTime): LocalTime {
    return time.fold(this) { acc, t ->
        if (acc.isAfter(t)) acc else t
    }
}