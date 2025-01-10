package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.EventSheet
import com.meetgom.backend.type.EventDateType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity(name = "event_sheet")
class EventSheetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", length = 256)
    val name: String,

    @Column(name = "description", length = 1024)
    val description: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_date_type")
    val eventDateType: EventDateType,

    @Column(name = "active_start_date")
    val activeStartDate: Date?,

    @Column(name = "active_end_date")
    val activeEndDate: Date?,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: Date? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: Date? = null,

    @Column(name = "is_active")
    val isActive: Boolean,

    @ManyToOne
    @JoinColumn(name = "time_zone_id")
    val timeZoneEntity: TimeZoneEntity,

    @OneToOne(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "event_code")
    val eventCode: EventCodeEntity,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "event_sheet_id")
    var eventSheetTimeSlotEntities: MutableList<EventSheetTimeSlotEntity>? = null,
) {
    fun toDomain(): EventSheet {
        return EventSheet(
            id = this.id,
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            activeStartDate = this.activeStartDate,
            activeEndDate = this.activeEndDate,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            isActive = this.isActive,
            timeZone = this.timeZoneEntity.toDomain(),
            eventCode = eventCode.toDomain(),
            eventSheetTimeSlots = this.eventSheetTimeSlotEntities?.map { it.toDomain() } ?: listOf()
        )
    }
}