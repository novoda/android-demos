package com.novoda.movies.mvi.search.presentation

import android.content.Context
import androidx.annotation.PluralsRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novoda.movies.mvi.search.R

internal class SearchResultCountViewHolder(
    private val label: TextView
) : RecyclerView.ViewHolder(label) {

    fun bind(resultCount: Int) {
        label.text = label.context.getQuantityString(R.plurals.search_result_count, resultCount)
    }

    companion object {
        fun create(parent: ViewGroup): SearchResultCountViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_result_count, parent, false)
            return SearchResultCountViewHolder(view as TextView)
        }
    }
}

fun Context.getQuantityString(@PluralsRes pluralsId: Int, count: Int): CharSequence {
    return resources.getQuantityString(pluralsId, count, count)
}
