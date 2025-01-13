package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.EventSheet
import com.meetgom.backend.type.EventDateType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

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

    @OneToOne(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "event_code")
    val eventCode: EventCodeEntity,

    // Host TimeZone
    @ManyToOne
    @JoinColumn(name = "host_time_zone_id")
    val hostTimeZoneEntity: TimeZoneEntity,

    // Host TimeZone 기준
    @Column(name = "active_start_date_time")
    val activeStartDateTime: LocalDateTime?,

    // Host TimeZone 기준
    @Column(name = "active_end_date_time")
    val activeEndDateTime: LocalDateTime?,

    // Host TimeZone 기준
    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "event_sheet_id")
    var eventSheetTimeSlotEntities: MutableList<EventSheetTimeSlotEntity>? = null,

    // Server 기준
    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

    // Server 기준
    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(name = "manual_active")
    val manualActive: Boolean,
) {
    fun toDomain(): EventSheet {
        return EventSheet(
            id = this.id,
            name = this.name,
            description = this.description,
            eventDateType = this.eventDateType,
            activeStartDateTime = this.activeStartDateTime,
            activeEndDateTime = this.activeEndDateTime,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            manualActive = this.manualActive,
            timeZone = this.hostTimeZoneEntity.toDomain(),
            hostTimeZone = this.hostTimeZoneEntity.toDomain(),
            eventCode = eventCode.toDomain(),
            eventSheetTimeSlots = this.eventSheetTimeSlotEntities?.map { it.toDomain() } ?: listOf()
        )
    }
}