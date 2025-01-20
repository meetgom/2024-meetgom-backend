package com.meetgom.backend.utils.listener

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class ApplicationStartedEventListener : ApplicationListener<ApplicationStartedEvent> {
    companion object {
        var isStarted = false
        var startTime: ZonedDateTime? = null
    }

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        isStarted = true
        startTime = ZonedDateTime.now()
    }
}