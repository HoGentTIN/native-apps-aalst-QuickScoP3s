package com.quickdev.projectdashboard.util.converters

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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

class DateTimeConverter {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) null else LocalDateTime.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromDate(date: LocalDateTime?): String? {
        return if (date == null) null else formatter.format(date)
    }
}

class DateAdapter {
    private val regex = Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @FromJson
    fun fromString(dateString: String): LocalDateTime {
        var toBeParsed = dateString

        val result = regex.find(dateString) // Ignore all that comes after the seconds
        if (result != null)
            toBeParsed = result.value

        return LocalDateTime.parse(toBeParsed, formatter)
    }

    @ToJson
    fun toString(date: LocalDateTime): String = formatter.format(date)
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