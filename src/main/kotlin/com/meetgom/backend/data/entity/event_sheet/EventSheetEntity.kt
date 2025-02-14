package com.meetgom.backend.data.entity.event_sheet

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.meetgom.backend.data.entity.common.TimeZoneEntity
import com.meetgom.backend.data.entity.participant.ParticipantEntity
import com.meetgom.backend.domain.model.event_sheet.EventSheet
import com.meetgom.backend.type.EventSheetType
import com.meetgom.backend.utils.extends.toTimeZone
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity(name = "event_sheet")
class EventSheetEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", length = 256)
    var name: String,

    @Column(name = "description", length = 1024)
    var description: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_sheet_type")
    val eventSheetType: EventSheetType,

    @OneToOne(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "event_sheet_code")
    val eventSheetCodeEntity: EventSheetCodeEntity,

    @ManyToOne
    @JoinColumn(name = "time_zone_id")
    val timeZoneEntity: TimeZoneEntity,

    @ManyToOne
    @JoinColumn(name = "host_time_zone_id")
    val hostTimeZoneEntity: TimeZoneEntity,

    @Column(name = "active_start_date_time")
    var activeStartDateTime: ZonedDateTime?,

    @Column(name = "active_end_date_time")
    var activeEndDateTime: ZonedDateTime?,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "event_sheet_id")
    @JsonManagedReference
    var eventSheetTimeSlotEntities: MutableList<EventSheetTimeSlotEntity>,

    @Column(name = "deleted_at")
    val deletedAt: ZonedDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: ZonedDateTime? = null,

    @CreationTimestamp
    @Column(name = "created_at")
    val createdAt: ZonedDateTime? = null,

    @Column(name = "manual_active")
    var manualActive: Boolean? = false,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "event_sheet_id")
    @JsonManagedReference
    var participantEntities: MutableList<ParticipantEntity>? = null,
) {
    fun update(
        name: String?,
        description: String?,
        activeStartDateTime: ZonedDateTime?,
        activeEndDateTime: ZonedDateTime?,
        manualActive: Boolean?
    ) {
        this.updatedAt = ZonedDateTime.now()
        this.name = name ?: this.name
        this.description = description ?: this.description
        this.activeStartDateTime =
            activeStartDateTime?.toTimeZone(this.timeZoneEntity.toDomain()) ?: this.activeStartDateTime
        this.activeEndDateTime =
            activeEndDateTime?.toTimeZone(this.timeZoneEntity.toDomain()) ?: this.activeEndDateTime
        this.manualActive = manualActive ?: this.manualActive
    }

    fun toDomain(): EventSheet {
        val eventSheet = EventSheet(
            id = this.id,
            name = this.name,
            description = this.description,
            eventSheetType = this.eventSheetType,
            activeStartDateTime = this.activeStartDateTime,
            activeEndDateTime = this.activeEndDateTime,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            manualActive = this.manualActive,
            timeZone = this.timeZoneEntity.toDomain(),
            hostTimeZone = this.hostTimeZoneEntity.toDomain(),
            eventSheetCode = eventSheetCodeEntity.toDomain(),
            eventSheetTimeSlots = this.eventSheetTimeSlotEntities.map { it.toDomain() },
            participants = this.participantEntities?.mapNotNull { if (it.deletedAt == null && it.userEntity.deletedAt == null) it.toDomain() else null }
                ?: emptyList(),
        )
        return eventSheet.convertTimeZone()
    }
}