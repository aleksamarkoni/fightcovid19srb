package com.covidvolonter.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.covidvolonter.main.feedback_adapter.FeedbackItem
import com.covidvolonter.main.feedback_adapter.FeedbackItemType
import com.covidvolonter.remote.repository.PoiRepository
import com.covidvolonter.repo.NotesRepo
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class FeedbacksViewModel @Inject constructor(private val poiRepository: PoiRepository) :
    ViewModel() {

    private val _notesList = MutableLiveData<List<NotesRepo>>()

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val notesList: LiveData<List<NotesRepo>>
        get() = _notesList

    fun fetchDummyFeedbacks(): List<FeedbackItemType> {
        val finalList = mutableListOf<FeedbackItemType>()

        for (i in 0 until 20) {
            finalList.add(FeedbackItem("Baba i deda su dobro $i", "20.03.2020."))
        }
        return finalList
    }

    fun fetchNotes(poiId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = poiRepository.getNotesForPoi(poiId)
                _notesList.value = result
            } catch (e: IOException) {

            } finally {
                _isLoading.value = false
            }
        }
    }
}