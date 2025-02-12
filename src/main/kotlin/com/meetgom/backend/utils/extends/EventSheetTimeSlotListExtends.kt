package com.meetgom.backend.utils.extends

import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.type.EventSheetType
import java.time.temporal.ChronoUnit

fun List<EventSheetTimeSlot>.sorted(eventSheetType: EventSheetType): List<EventSheetTimeSlot> {
    return when (eventSheetType) {
        EventSheetType.SPECIFIC_DATES -> {
            this.sortedWith(EventSheetTimeSlot.comparatorForSpecificDates())
                .fold(mutableListOf()) { acc, timeSlot ->
                    if (acc.isEmpty())
                        acc.add(timeSlot)
                    else {
                        val last = acc.last()
                        val diff = ChronoUnit.MINUTES.between(last.endTime, timeSlot.startTime)
                        if (last.date == timeSlot.date && diff <= 0L) { // 끝나는 시간과 시작 시간이 1분 미만 차이나거나 더 큰 경우, 합침
                            acc.removeLast()
                            acc.add(
                                EventSheetTimeSlot(
                                    eventSheetId = last.eventSheetId,
                                    date = timeSlot.date,
                                    startTime = last.startTime,
                                    endTime = last.endTime.max(timeSlot.endTime)
                                )
                            )
                        } else {
                            acc.add(timeSlot)
                        }
                    }
                    acc
                }
        }

        EventSheetType.RECURRING_WEEKDAYS -> {
            val dayOfWeekMap = this.groupBy { it.date.dayOfWeek }.mapValues { entry ->
                entry.value.minBy { it.date }.date
            }
            this.sortedWith(EventSheetTimeSlot.comparatorForRecurringWeekdays())
                .fold(mutableListOf()) { acc, timeSlot ->
                    if (acc.isEmpty())
                        acc.add(
                            EventSheetTimeSlot(
                                eventSheetId = timeSlot.eventSheetId,
                                date = dayOfWeekMap[timeSlot.date.dayOfWeek]!!,
                                startTime = timeSlot.startTime,
                                endTime = timeSlot.endTime
                            )
                        )
                    else {
                        val last = acc.last()
                        val diff = ChronoUnit.MINUTES.between(last.endTime, timeSlot.startTime)
                        if (last.date.dayOfWeek == timeSlot.date.dayOfWeek && diff <= 0L) { // 끝나는 시간과 시작 시간이 1분 미만 차이나거나 더 큰 경우, 합침
                            acc.removeLast()
                            acc.add(
                                EventSheetTimeSlot(
                                    eventSheetId = last.eventSheetId,
                                    date = dayOfWeekMap[timeSlot.date.dayOfWeek]!!,
                                    startTime = last.startTime,
                                    endTime = last.endTime.max(timeSlot.endTime)
                                )
                            )
                        } else {
                            acc.add(timeSlot)
                        }
                    }
                    acc
                }
        }
    }
}