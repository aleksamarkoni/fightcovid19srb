package com.volontero.repo

import com.volontero.remote.Note
import com.volontero.util.BaseUiModel
import com.volontero.util.DateTimeUtil

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