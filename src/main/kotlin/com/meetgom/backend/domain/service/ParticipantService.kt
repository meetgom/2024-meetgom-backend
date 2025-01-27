package com.meetgom.backend.domain.service

import com.meetgom.backend.data.repository.EventSheetRepository
import com.meetgom.backend.data.repository.ParticipantRepository
import com.meetgom.backend.data.repository.ParticipantRoleRepository
import com.meetgom.backend.data.repository.TimeZoneRepository
import org.springframework.stereotype.Service

@Service
class ParticipantService(
    private val userService: UserService,
    private val eventSheetRepository: EventSheetRepository,
    private val timeZoneRepository: TimeZoneRepository,
    private val participantRepository: ParticipantRepository,
    private val participantRoleRepository: ParticipantRoleRepository,
) {

}