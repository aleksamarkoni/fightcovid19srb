package com.covidvolonter.util

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

object DateTimeUtil {
    fun convertUtcToLocal(datetime: String): LocalDateTime {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(datetime, dateTimeFormatter)
        val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
        return ZonedDateTime.ofInstant(offsetDateTime.toInstant(), ZoneId.systemDefault())
            .toLocalDateTime()
    }
}