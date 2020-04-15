package com.volontero.main.feedback_adapter

import android.view.View
import com.volontero.repo.NoNotes
import com.volontero.util.BaseUiModel
import com.volontero.util.ViewHolder

class NoFeedbackItemViewHolder(itemView: View) : ViewHolder(itemView) {

    private var model: NoNotes? = null

    override fun onBindViewHolder(model: BaseUiModel) {
        model as NoNotes
        this.model = model
    }
}