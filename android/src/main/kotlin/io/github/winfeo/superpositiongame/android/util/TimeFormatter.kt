package io.github.winfeo.superpositiongame.android.util

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object TimeFormatter {
    private val timePattern = DateTimeFormatter.ofPattern("HH:mm")
    private val dateTimePattern = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun formatTime(iso: String): String = format(iso, timePattern)
    fun formatDateTime(iso: String): String = format(iso, dateTimePattern)

    private fun format(
        iso: String,
        formatter: DateTimeFormatter
    ): String {
        return try {
            LocalDateTime.parse(iso, DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(formatter)
        } catch (_: DateTimeParseException) {
            try {
                OffsetDateTime.parse(iso).format(formatter)
            } catch (_: DateTimeParseException) {
                iso
            }
        }
    }
}
