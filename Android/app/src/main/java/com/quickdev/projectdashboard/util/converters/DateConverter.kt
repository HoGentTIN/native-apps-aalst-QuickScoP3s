package com.quickdev.projectdashboard.util.converters

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.*
import java.time.format.DateTimeFormatter

class DateConverter {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return if (dateString == null) null else LocalDate.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromDate(date: LocalDate?): String? {
        return if (date == null) null else formatter.format(date)
    }
}

class DateAdapter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @FromJson
    fun fromString(dateString: String): LocalDate =
        LocalDate.parse(dateString, formatter)

    @ToJson
    fun toString(date: LocalDate): String =
        formatter.format(LocalDateTime.of(date, LocalTime.MIDNIGHT)) // LocalDate ondersteunt geen uurformat, dus dit is een workaround via LocalDateTime
}

object DateFormatter {

    fun fromString(dateString: String, pattern: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDate.parse(dateString, formatter)
    }

    fun toString(date: LocalDate, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return formatter.format(date)
    }

    fun fromLong(date: Long): LocalDate {
        return Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun toLong(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}