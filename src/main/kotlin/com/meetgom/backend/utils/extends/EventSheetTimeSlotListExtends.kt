package com.meetgom.backend.utils.extends

import com.meetgom.backend.domain.model.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.type.EventSheetType
import java.time.temporal.ChronoUnit

fun List<EventSheetTimeSlot>.alignTimeSlots(): List<EventSheetTimeSlot> {
    return this.sorted()
        .fold(mutableListOf()) { acc, timeSlot ->
            if (acc.isEmpty()) {
                acc.add(timeSlot)
            } else {
                val last = acc.last()
                val diff = (ChronoUnit.MINUTES.between(last.endTime, timeSlot.startTime) % 60).toInt()
                if (last.date == timeSlot.date && (diff == 0 || last.endTime.isBefore(timeSlot.startTime))) {
                    acc.removeLast()
                    acc.add(
                        EventSheetTimeSlot(
                            date = last.date,
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

fun List<EventSheetTimeSlot>.sorted(eventSheetType: EventSheetType): List<EventSheetTimeSlot> {
    return when (eventSheetType) {
        EventSheetType.SPECIFIC_DATES -> this.alignTimeSlots()
        EventSheetType.RECURRING_WEEKDAYS -> this.alignTimeSlots()
            .sortedWith(
                compareBy(
                    { it.date.dayOfWeek.ordinal },
                    { it.startTime },
                    { it.endTime }
                )
            )
    }
}