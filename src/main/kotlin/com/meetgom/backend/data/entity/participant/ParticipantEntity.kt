package com.meetgom.backend.data.entity.participant

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.meetgom.backend.data.entity.common.TimeZoneEntity
import com.meetgom.backend.data.entity.event_sheet.EventSheetEntity
import com.meetgom.backend.data.entity.user.UserEntity
import com.meetgom.backend.domain.model.participant.Participant
import com.meetgom.backend.domain.model.participant.ParticipantAvailableTimeSlot
import com.meetgom.backend.type.UserType
import jakarta.persistence.*
import java.time.ZonedDateTime


@Entity(name = "participant")
class ParticipantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "participant_name")
    var participantName: String? = null,

    @ManyToOne
    @JoinColumn(name = "event_sheet_id")
    @JsonBackReference
    val eventSheetEntity: EventSheetEntity,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val userEntity: UserEntity,

    @ManyToOne
    @JoinColumn(name = "participant_role")
    val roleEntity: ParticipantRoleEntity,

    @ManyToOne
    @JoinColumn(name = "participant_time_zone_id")
    var timeZoneEntity: TimeZoneEntity,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "participant_id")
    @JsonManagedReference
    var availableTimeSlotEntities: MutableList<ParticipantAvailableTimeSlotEntity>,

    @Column(name = "deleted_at")
    val deletedAt: ZonedDateTime? = null,
) {
    fun update(
        userName: String?,
        timeZoneEntity: TimeZoneEntity?,
        availableTimeSlots: List<ParticipantAvailableTimeSlot>?
    ) {
        if (userName != null) {
            when (userEntity.userType) {
                UserType.ANONYMOUS -> this.userEntity.userName = userName
                UserType.STANDARD -> this.participantName = userName
            }
        }
        this.timeZoneEntity = timeZoneEntity ?: this.timeZoneEntity
        if (availableTimeSlots != null) {
            this.availableTimeSlotEntities.clear()
            availableTimeSlots.forEach { tempSlot ->
                val newSlot = tempSlot.toEntity()
                newSlot.participantEntity = this
                this.availableTimeSlotEntities.add(newSlot)
            }
        }
    }

    fun toDomain(): Participant {
        return Participant(
            id = this.id,
            participantName = this.participantName,
            eventSheetCode = this.eventSheetEntity.eventSheetCodeEntity.eventSheetCode,
            user = userEntity.toDomain(),
            role = roleEntity.participantRole,
            timeZone = timeZoneEntity.toDomain(),
            availableTimeSlots = availableTimeSlotEntities.map { it.toDomain() }
        )
    }
}