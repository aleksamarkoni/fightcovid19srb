package com.covidvolonter.repo

import com.covidvolonter.remote.Note
import com.covidvolonter.remote.PoiDetail
import com.covidvolonter.util.DateTimeUtil.convertUtcToLocal

data class PoiDetailRepo(
    val address: String,
    val apartment: String,
    val poiNote: String,
    val lastVisitDate: String,
    val lastVisitFeedback: String,
    val phone: String?,
    val poiCreator: String,
    val profileImage: String?
) {
    companion object {
        fun map(poiDetailResponse: PoiDetail) =
            PoiDetailRepo(
                poiDetailResponse.address,
                poiDetailResponse.apartment,
                if (poiDetailResponse.note.isBlank()) "No special notes" else poiDetailResponse.note,
                getLastVisitDate(poiDetailResponse.notes),
                getLastFeedback(poiDetailResponse.notes),
                poiDetailResponse.phone,
                poiDetailResponse.creator.name,
                poiDetailResponse.creator.picture
            )

        private fun getLastFeedback(notes: List<Note>): String {
            if (notes.isNullOrEmpty()) {
                return "No feedback"
            }

            val localDateTime = convertUtcToLocal(notes.first().date)
            var currentFeedback = notes.first().text ?: "No feedback"

            for (item in notes) {
                val currentTime = convertUtcToLocal(item.date)

                if (currentTime.isAfter(localDateTime)) currentFeedback =
                    item.text ?: "No feedback"
            }
            return currentFeedback
        }

        private fun getLastVisitDate(notes: List<Note>): String {

            if (notes.isNullOrEmpty()) {
                return "This person has not been visited yet"
            }

            var localDateTime = convertUtcToLocal(notes.first().date)

            for (item in notes) {
                val currentTime = convertUtcToLocal(item.date)

                if (currentTime.isAfter(localDateTime)) localDateTime = currentTime
            }

            return localDateTime.toLocalDate().toString()
        }
    }
}