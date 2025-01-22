package com.meetgom.backend.entity.event_sheet

import com.meetgom.backend.model.domain.event_sheet.EventCode
import jakarta.persistence.*


@Entity(name = "event_code")
class EventCodeEntity(
    @Id
    @Column(name = "event_code", length = 256)
    val eventCode: String,

    @Column(name = "pin_code", length = 256)
    val pinCode: String,

    @Column(name = "salt", length = 256)
    val salt: String,

    @OneToOne(
        mappedBy = "eventCode",
        cascade = [CascadeType.ALL]
    )
    val eventSheetEntity: EventSheetEntity? = null,
) {
    fun toDomain(): EventCode {
        return EventCode(
            eventCode = this.eventCode,
            pinCode = this.pinCode,
            salt = this.salt,
        )
    }
}