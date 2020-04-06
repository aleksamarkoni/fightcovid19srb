package com.covidvolonter.repo

import com.covidvolonter.remote.Note
import com.covidvolonter.util.BaseUiModel
import com.covidvolonter.util.DateTimeUtil

data class NotesRepo(
    val note: String?,
    val imageUrl: String?,
    val name: String,
    val date: String
) : BaseUiModel {
    companion object {
        fun map(note: Note): NotesRepo = NotesRepo(
            note.text,
            note.creatorId.picture,
            note.creatorId.name,
            DateTimeUtil.convertUtcToString(note.date)
        )
    }
}