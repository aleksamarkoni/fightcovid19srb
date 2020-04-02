package com.fightcovid.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateTimeUtil {
    fun convertUtcToLocal(datetime: String): LocalDateTime {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.parse(datetime, dateTimeFormatter)
    }
}