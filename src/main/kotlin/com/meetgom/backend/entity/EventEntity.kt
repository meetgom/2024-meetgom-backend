package com.meetgom.backend.entity

import com.meetgom.backend.type.EventDateType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity(name = "event")
class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "name", length = 256)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_date_type")
    val eventDateType: EventDateType,

    @Column(name = "pin_code", length = 256, unique = true)
    val pinCode: String,

    @Column(name = "end_date")
    var endDate: Date?,

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Date?,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Date?,

    @OneToMany(targetEntity = EventRecurringWeekdayEntity::class)
    @JoinColumn(name = "event_id")
    var eventRecurringWeekdays: List<EventRecurringWeekdayEntity>,

    @OneToMany(targetEntity = EventSpecificDateEntity::class)
    @JoinColumn(name = "event_id")
    var eventSpecificDates: List<EventSpecificDateEntity>,

    @OneToMany(targetEntity = AccessKeyEntity::class)
    @JoinColumn(name = "event_id")
    var accessKeys: List<AccessKeyEntity>
)