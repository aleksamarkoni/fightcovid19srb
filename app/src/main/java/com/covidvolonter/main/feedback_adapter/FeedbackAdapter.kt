package com.covidvolonter.main.feedback_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.covidvolonter.repo.NotesRepo
import com.covidvolonter.util.ViewHolder
import com.google.fightcorona.R

class FeedbackAdapter() :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.visit_feedback_item, parent, false)
        return FeedbackItemViewHolder(
            view
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(items[position])
    }

    var items: List<NotesRepo> = mutableListOf()
        set(items) {
            field = items
            notifyDataSetChanged()
        }
}