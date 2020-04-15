package com.volontero.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView) {
    abstract fun onBindViewHolder(model: BaseUiModel)
}