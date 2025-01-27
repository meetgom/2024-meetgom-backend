package com.meetgom.backend.security.service

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