package com.meetgom.backend.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class TimeUtils {
    companion object {
        const val BASE_TIME_FORMAT = "HH:mm"

        fun timeStringToLocalTime(
            timeString: String,
            allowMidnight: Boolean = true,
            endOfDayToMidnight: Boolean = true
        ): LocalTime {
            if (allowMidnight.not() && timeString == "00:00")
                throw InternalError("00:00 is not allowed")
            if (endOfDayToMidnight && timeString == "24:00")
                return LocalTime.of(0, 0)
            return LocalTime.parse(timeString)
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