package com.meetgom.backend.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeUtils {
    companion object {
        val timeRegex: Regex = Regex("^(([01]?[0-9]|2[0-3]):[0-5][0-9])|(24:00)$")
        val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        fun timeStringToLocalTime(
            timeString: String,
        ): LocalTime {
            val trimmedTimeString = timeString.trim()
            if (!timeRegex.matches(trimmedTimeString))
                throw IllegalArgumentException("Invalid time format: $timeString")
            if (trimmedTimeString == "24:00")
                return LocalTime.MAX
            return LocalTime.parse(trimmedTimeString)
        }

        fun localTimeToTimeString(localTime: LocalTime): String {
            if (localTime == LocalTime.MAX)
                return "24:00"
            val formatted = localTime.format(timeFormat)
            return formatted
        }

        fun getClosestDayOfWeek(weekday: DayOfWeek): LocalDate {
            val today = LocalDate.now()
            val dayOfWeek = today.dayOfWeek
            val daysToTarget = weekday.ordinal - dayOfWeek.ordinal

            return today.plusDays(
                when {
                    daysToTarget < 0 -> daysToTarget + 7  // 이전 주의 해당 요일
                    daysToTarget > 0 -> daysToTarget  // 다음 주의 해당 요일
                    else -> 0  // 오늘이 이미 해당 요일
                }.toLong()
            )
        }
    }
}