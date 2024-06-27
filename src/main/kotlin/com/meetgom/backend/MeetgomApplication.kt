package com.meetgom.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MeetgomApplication

fun main(args: Array<String>) {
    runApplication<MeetgomApplication>(*args)
}
