package com.meetgom.backend.exception

import com.meetgom.backend.http.HttpException
import org.springframework.http.HttpStatus

open class InvalidArgumentException(message: String?) : HttpException(HttpStatus.BAD_REQUEST, message)

class InvalidPinCodeException: InvalidArgumentException("잘못된 핀코드입니다.")
class InvalidEventSheetTimeSlotsException : InvalidArgumentException("잘못된 시간 슬롯 형식입니다.")
class InvalidDayOfWeeksException : InvalidArgumentException("잘못된 요일 형식입니다.")
class InvalidTimeFormatException : InvalidArgumentException("잘못된 시간 형식입니다. (HH:mm)")