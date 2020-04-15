package com.volontero.main.feedback_adapter

import android.view.View
import com.bumptech.glide.Glide
import com.volontero.R
import com.volontero.repo.NotesRepo
import com.volontero.util.BaseUiModel
import com.volontero.util.ViewHolder
import kotlinx.android.synthetic.main.visit_feedback_item.view.*

class FeedbackItemViewHolder(itemView: View) : ViewHolder(itemView) {

    var model: NotesRepo? = null

    override fun onBindViewHolder(model: BaseUiModel) {
        model as NotesRepo
        this.model = model
        loadProfileImage(model.imageUrl)
        itemView.feedback_creator_name.text = model.name
        itemView.feedback_description_text.text = model.note
        itemView.feedback_date_text.text = model.date
    }

    private fun loadProfileImage(imageUrl: String?) {
        Glide.with(itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_person_purple_24dp)
            .into(itemView.profile_image_feedback_item)
    }
}