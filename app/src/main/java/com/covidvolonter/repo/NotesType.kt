package com.covidvolonter.repo

import com.covidvolonter.remote.Note
import com.covidvolonter.util.BaseUiModel
import com.covidvolonter.util.DateTimeUtil

sealed class NoteType(val noteTypeEnum: NoteTypeEnum) : BaseUiModel
data class NotesRepo(
    val note: String?,
    val imageUrl: String?,
    val name: String,
    val date: String
) : NoteType(NoteTypeEnum.NOTE_TYPE) {
    companion object {
        fun map(note: Note): NotesRepo = NotesRepo(
            note.text,
            note.creatorId.picture,
            note.creatorId.name,
            DateTimeUtil.convertUtcToString(note.date)
        )
    }
}

class NoNotes : NoteType(NoteTypeEnum.NO_NOTES)

enum class NoteTypeEnum {
    NOTE_TYPE,
    NO_NOTES
}