package com.meetgom.backend.exception

import com.meetgom.backend.http.HttpException
import org.springframework.http.HttpStatus

open class NotFoundException(message: String?) : HttpException(HttpStatus.NOT_FOUND, message)

class EventSheetNotFoundException : NotFoundException("이벤트 시트를 찾을 수 없습니다.")
class TimeZoneNotFoundException : NotFoundException("TimeZone을 찾을 수 없습니다.")
