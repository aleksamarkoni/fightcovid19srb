package com.covidvolonter.main.feedback_adapter

import android.view.View
import com.covidvolonter.util.BaseUiModel
import com.covidvolonter.util.ViewHolder
import kotlinx.android.synthetic.main.visit_feedback_item.view.*

class FeedbackItemViewHolder(itemView: View) : ViewHolder(itemView) {

    var model: FeedbackItem? = null

    override fun onBindViewHolder(model: BaseUiModel) {
        model as FeedbackItem
        this.model = model
        itemView.feedback_text_description.text = model.description
        itemView.feedback_date_text.text = model.date
    }
}