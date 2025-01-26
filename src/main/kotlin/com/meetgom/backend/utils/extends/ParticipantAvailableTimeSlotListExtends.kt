package com.meetgom.backend.utils.extends

import com.meetgom.backend.model.domain.event_sheet.EventSheetTimeSlot
import com.meetgom.backend.model.domain.participant.ParticipantAvailableTimeSlot
import java.time.temporal.ChronoUnit

fun List<ParticipantAvailableTimeSlot>.alignTimeSlots(): List<ParticipantAvailableTimeSlot> {
    return this.sorted()
        .fold(mutableListOf<ParticipantAvailableTimeSlot>()) { acc, timeSlot ->
            if (acc.isEmpty()) {
                acc.add(timeSlot)
            } else {
                val last = acc.last()
                val diff = (ChronoUnit.MINUTES.between(last.endTime, timeSlot.startTime) % 60).toInt()
                if (last.date == timeSlot.date && (diff == 0 || last.endTime.isBefore(timeSlot.startTime))) {
                    acc.removeLast()
                    acc.add(
                        ParticipantAvailableTimeSlot(
                            participantId = last.participantId,
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