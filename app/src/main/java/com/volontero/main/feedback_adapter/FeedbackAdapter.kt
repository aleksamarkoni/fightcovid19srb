package com.volontero.main.feedback_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volontero.R
import com.volontero.repo.NoteType
import com.volontero.repo.NoteTypeEnum
import com.volontero.util.ViewHolder

class FeedbackAdapter :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            NoteTypeEnum.NOTE_TYPE.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.visit_feedback_item, parent, false)
                FeedbackItemViewHolder(
                    view
                )
            }
            NoteTypeEnum.NO_NOTES.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.no_feedback_item, parent, false)
                NoFeedbackItemViewHolder(
                    view
                )
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.no_feedback_item, parent, false)
                NoFeedbackItemViewHolder(
                    view
                )
            }
        }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].noteTypeEnum.ordinal

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(items[position])
    }

    var items: List<NoteType> = mutableListOf()
        set(items) {
            field = items
            notifyDataSetChanged()
        }
}