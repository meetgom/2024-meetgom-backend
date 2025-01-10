package com.meetgom.backend.entity

import com.meetgom.backend.model.domain.EventCode
import jakarta.persistence.*


@Entity(name = "event_code")
class EventCodeEntity(
    @Id
    @Column(name = "event_code", length = 256)
    val eventCode: String,

    @OneToOne(
        mappedBy = "eventCode",
        cascade = [CascadeType.ALL]
    )
    val eventSheetEntity: EventSheetEntity? = null,
) {
    fun toDomain(): EventCode {
        return EventCode(
            eventCode = this.eventCode,
        )
    }
}