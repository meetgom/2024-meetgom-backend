package com.meetgom.backend.utils.utils

import com.meetgom.backend.exception.common.CommonExceptions
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeUtils {
    companion object {
        private val timeRegex: Regex = Regex("^(([01]?[0-9]|2[0-3]):[0-5][0-9])|(24:00)$")
        private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val MIN_LOCAL_TIME: LocalTime = LocalTime.of(0, 0, 0)
        val MAX_LOCAL_TIME: LocalTime = LocalTime.of(23, 59, 59)

        fun timeStringToLocalTime(
            timeString: String,
        ): LocalTime {
            val trimmedTimeString = timeString.trim()
            if (!timeRegex.matches(trimmedTimeString))
                throw CommonExceptions.INVALID_TIME_FORMAT.toException()
            if (trimmedTimeString == "24:00")
                return MAX_LOCAL_TIME
            return LocalTime.parse(trimmedTimeString)
        }

        fun localTimeToTimeString(localTime: LocalTime): String {
            if (localTime == MAX_LOCAL_TIME)
                return "24:00"
            val formatted = localTime.format(timeFormat)
            return formatted
        }

        fun getDayOfNextWeek(weekday: DayOfWeek): LocalDate {
            val today = LocalDate.now()
            val dayOfWeek = today.dayOfWeek
            val daysUntilNextWeek = 7 - dayOfWeek.ordinal + weekday.ordinal
            return today.plusDays(daysUntilNextWeek.toLong())
        }
    }
}