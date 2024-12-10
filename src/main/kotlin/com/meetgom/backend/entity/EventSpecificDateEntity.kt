package com.meetgom.backend.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate


@Entity(name = "event_specific_date")
class EventSpecificDateEntity(
    @EmbeddedId
    val eventSpecificDatePK: EventSpecificDatePrimaryKey,

    @Column(name = "start_time")
    var startTime: Int,

    @Column(name = "end_time")
    var endTime: Int
)


@Embeddable
data class EventSpecificDatePrimaryKey(
    @ManyToOne(targetEntity = EventEntity::class)
    @JoinColumn(name = "event_id")
    val event: EventEntity,

    @Column(name = "date")
    val date: LocalDate
) : Serializable