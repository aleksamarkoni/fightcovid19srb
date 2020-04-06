package com.covidvolonter.main.feedback_adapter

import android.view.View
import com.covidvolonter.repo.NoNotes
import com.covidvolonter.util.BaseUiModel
import com.covidvolonter.util.ViewHolder

class NoFeedbackItemViewHolder(itemView: View) : ViewHolder(itemView) {

    private var model: NoNotes? = null

    override fun onBindViewHolder(model: BaseUiModel) {
        model as NoNotes
        this.model = model
    }
}