package com.meetgom.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MeetgomApplication

fun main(args: Array<String>) {
    runApplication<MeetgomApplication>(*args)
}
