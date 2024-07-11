package com.meetgom.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "event")
data class Event(
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

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val availableDates: List<EventAvailableDate>? = null,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val availableDaysOfWeek: List<EventAvailableDayOfWeek>? = null,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val accessCodes: List<EventAccessCode>? = null
)

enum class EventDateType {
    SPECIFIC_DATES, RECURRING_WEEKDAYS
}