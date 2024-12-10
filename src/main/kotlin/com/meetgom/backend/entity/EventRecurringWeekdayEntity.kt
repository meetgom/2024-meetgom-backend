package com.meetgom.backend.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity(name = "event_recurring_weekday")
class EventRecurringWeekdayEntity(
    @EmbeddedId
    val eventRecurringWeekdayPK: EventRecurringWeekdayPrimaryKey,

    @Column(name = "start_time")
    var startTime: Int,

    @Column(name = "end_time")
    var endTime: Int
)


@Embeddable
data class EventRecurringWeekdayPrimaryKey(
    @ManyToOne(targetEntity = EventEntity::class)
    @JoinColumn(name = "event_id")
    val event: EventEntity,

    @Column(name = "weekday")
    val weekday: Int
) : Serializable