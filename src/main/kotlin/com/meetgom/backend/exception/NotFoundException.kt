package com.meetgom.backend.exception

import com.meetgom.backend.http.HttpException
import org.springframework.http.HttpStatus

open class NotFoundException(message: String?) : HttpException(HttpStatus.NOT_FOUND, message)

class EventSheetNotFoundException : NotFoundException("Event Sheet not found")
class TimeZoneNotFoundException : NotFoundException("Time Zone not found")
