package com.covidvolonter.repo

import com.covidvolonter.remote.Note
import com.covidvolonter.util.BaseUiModel
import com.covidvolonter.util.DateTimeUtil
import org.threeten.bp.LocalDateTime

data class NotesRepo(
    val note: String?,
    val imageUrl: String?,
    val name: String,
    val date: LocalDateTime
) : BaseUiModel {
    companion object {
        fun map(note: Note): NotesRepo = NotesRepo(
            note.text,
            note.creatorId.picture,
            note.creatorId.name,
            DateTimeUtil.convertUtcToLocal(note.date)
        )
    }
}