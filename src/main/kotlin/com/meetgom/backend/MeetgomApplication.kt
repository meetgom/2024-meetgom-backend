package com.meetgom.backend

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MeetgomApplication

@PostConstruct
fun configure(){
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
}

fun main(args: Array<String>) {
    runApplication<MeetgomApplication>(*args)
}
