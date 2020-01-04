package com.quickdev.projectdashboard.util.converters

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalTime
import java.time.format.DateTimeParseException

class TimeConverterTest {

	private val timeConverter = TimeConverter()

	@Test
	fun toTime_nullString_returnsNull() {
		val input: String? = null
		val result = timeConverter.toTime(input)

		assertNull(result)
	}

	@Test
	fun toTime_validString_returnsDate() {
		val input: String? = "15:39:00"
		val result = timeConverter.toTime(input)

		val localTime = LocalTime.of(15, 39)

		assertNotNull(result)
		assertEquals(result!!, localTime) // Actual werd reeds een maal berekend via een andere methode
	}

	@Test(expected = DateTimeParseException::class)
	fun toTime_invalidString_throwException() {
		val input: String? = "Dit is invalid"
		timeConverter.toTime(input)
	}

	@Test
	fun fromTime_nullDate_returnsNull() {
		val input: LocalTime? = null
		val result = timeConverter.fromTime(input)

		assertNull(result)
	}

	@Test
	fun fromTime_validDate_returnsString() {
		val input = LocalTime.of(15,39)
		val result = timeConverter.fromTime(input)

		assertNotNull(result)
		assertEquals(result, "15:39:00")
	}
}

class TimeAdapterTest {

	private val timeAdapter = TimeAdapter()

	@Test
	fun fromString_validString_returnsDate() {
		val input = "05:10:00"
		val result = timeAdapter.fromString(input)

		val localDate = LocalTime.of(5,10)
		assertEquals(result, localDate)
	}

	@Test(expected = DateTimeParseException::class)
	fun fromString_invalidString_throwException() {
		val input = "Dit is invalid"
		timeAdapter.fromString(input)
	}

	@Test
	fun fromDate_validDate_returnsString() {
		val input = LocalTime.of(5,10)
		val result = timeAdapter.toString(input)

		assertEquals(result, "05:10:00")
	}


}