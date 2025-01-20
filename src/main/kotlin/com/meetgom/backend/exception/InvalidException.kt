package com.meetgom.backend.exception

import com.meetgom.backend.http.HttpException
import org.springframework.http.HttpStatus

open class InvalidArgumentException(message: String?) : HttpException(HttpStatus.BAD_REQUEST, message)

class InvalidEventSheetTimeSlotsException : InvalidArgumentException("Invalid Event Sheet Time Slots")
class InvalidDayOfWeeksException : InvalidArgumentException("Invalid Day of Weeks")
class InvalidTimeFormatException : InvalidArgumentException("Invalid Time Format")