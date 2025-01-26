package com.meetgom.backend.security.service

import com.meetgom.backend.entity.participant.ParticipantEntity
import com.meetgom.backend.model.domain.participant.Participant
import lombok.Getter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/*
@Getter
class ParticipantDetailsImpl(
    private val eventSheetCode: String,
    private val userId: Long,
    private val displayName: String,
    private val password: String?,
    private val authorities: Collection<GrantedAuthority>
) {
    companion object {
        fun build(participant: Participant): ParticipantDetailsImpl {
            val eventSheetCode = participant.eventSheetCode
            val user = participant.user
            val authorities: GrantedAuthority = SimpleGrantedAuthority(
                participant.role.role.name
            )

            return ParticipantDetailsImpl(
                eventSheetCode = eventSheetCode,
                userId =
                    user.password,
                authorities
            )
        }
    }
}

 */