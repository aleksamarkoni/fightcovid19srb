package com.fightcovid.main.view_models

import androidx.lifecycle.ViewModel
import com.fightcovid.main.feedback_adapter.FeedbackItem
import com.fightcovid.main.feedback_adapter.FeedbackItemType
import javax.inject.Inject

class FeedbacksViewModel @Inject constructor() : ViewModel() {

    fun fetchDummyFeedbacks(): List<FeedbackItemType> {
        val finalList = mutableListOf<FeedbackItemType>()

        for (i in 0 until 20) {
            finalList.add(FeedbackItem("Baba i deda su dobro $i", "20.03.2020."))
        }
        return finalList
    }
}