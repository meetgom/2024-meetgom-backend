package com.meetgom.backend.utils

class TimeUtil private constructor() {
    companion object {
        fun timeStringToTimeInt(timeString: String): Int {
            val time = timeString.split(":")
            return time[0].toInt() * 60 + time[1].toInt()
        }

        fun intTimeToTimeString(timeInt: Int): String {
            val hour = timeInt / 60
            val minute = timeInt % 60
            return "%02d:%02d".format(hour, minute)
        }
    }
}
