package com.meetgom.backend.util

import com.meetgom.backend.model.Weekday

fun encodeDaysToByte(days: List<Weekday>): Byte {
    var bitMask = 0
    days.forEach { day ->
        bitMask = bitMask or day.value
    }
    return bitMask.toByte()
}