package com.meetgom.backend.entity

import com.meetgom.backend.model.EventDateType
import jakarta.persistence.*

@Entity
@Table(name = "event")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(length = 50)
    val timeZone: String,

    @Column(length = 100)
    val title: String,

    @Enumerated(EnumType.STRING)
    val eventDateType: EventDateType,

    val startTime: Int = 0,

    val endTime: Int = 0,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val availableDates: MutableList<EventAvailableDateEntity> = mutableListOf(),

    @OneToOne(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var availableDaysOfWeek: EventAvailableDayOfWeekEntity? = null,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val accessCodes: MutableList<EventAccessCodeEntity> = mutableListOf()
)