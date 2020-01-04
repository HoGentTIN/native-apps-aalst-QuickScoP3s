package com.quickdev.projectdashboard.util.converters

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeConverter {

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    @TypeConverter
    fun toTime(timeString: String?): LocalTime? {
        return if (timeString == null) null else LocalTime.parse(timeString, formatter)
    }

    @TypeConverter
    fun fromTime(time: LocalTime?): String? {
        return if (time == null) null else formatter.format(time)
    }
}

class TimeAdapter {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    @FromJson
    fun fromString(timeString: String): LocalTime =
        LocalTime.parse(timeString, formatter)

    @ToJson
    fun toString(time: LocalTime): String =
        formatter.format(time)
}

object TimeFormatter {

    fun fromString(dateString: String, pattern: String): LocalTime {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalTime.parse(dateString, formatter)
    }

    fun toString(time: LocalTime, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return formatter.format(time)
    }

    fun fromLong(time: Long): LocalTime {
        return LocalTime.ofNanoOfDay(time)
    }

    fun toLong(time: LocalTime): Long {
        return time.toNanoOfDay()
    }
}