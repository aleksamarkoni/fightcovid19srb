package com.volontero.util

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

object DateTimeUtil {
    fun convertUtcToLocal(datetime: String): LocalDateTime {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMATTER)
        val localDateTime = LocalDateTime.parse(datetime, dateTimeFormatter)
        val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
        return ZonedDateTime.ofInstant(offsetDateTime.toInstant(), ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun convertUtcToString(datetime: String): String {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMATTER)
        val localDateTime = LocalDateTime.parse(datetime, dateTimeFormatter)
        val offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC)
        val finalDateTime =
            ZonedDateTime.ofInstant(offsetDateTime.toInstant(), ZoneId.systemDefault())
                .toLocalDateTime()
        val finalFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER)
        return finalDateTime.format(finalFormatter)
    }
}