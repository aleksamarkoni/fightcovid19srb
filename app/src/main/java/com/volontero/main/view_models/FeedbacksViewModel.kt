package com.volontero.main.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volontero.remote.repository.PoiRepository
import com.volontero.repo.NoteType
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class FeedbacksViewModel @Inject constructor(private val poiRepository: PoiRepository) :
    ViewModel() {

    private val _notesList = MutableLiveData<List<NoteType>>()

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val notesList: LiveData<List<NoteType>>
        get() = _notesList

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