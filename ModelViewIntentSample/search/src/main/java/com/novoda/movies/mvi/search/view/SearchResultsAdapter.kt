package com.novoda.movies.mvi.search.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

internal class SearchResultsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private enum class SearchResultType(val id: Int) {
        RESULT_COUNT(0),
        ITEM(1)
    }

    private val movies = mutableListOf<ViewSearchItem>()
    private val headerCount = 1

    private var totalItemCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchResultType.RESULT_COUNT.id -> SearchResultCountViewHolder.create(
                parent
            )
            else -> SearchResultItemViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> SearchResultType.RESULT_COUNT
            else -> SearchResultType.ITEM
        }.id
    }

    override fun getItemCount() = movies.size + headerCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchResultItemViewHolder -> {
                val movie = movies[position - headerCount]
                holder.bind(movie)
            }

            is SearchResultCountViewHolder -> holder.bind(totalItemCount)
        }
    }

    fun bind(results: ViewSearchResults) {
        this.movies.clear()
        this.movies.addAll(results.items)
        this.totalItemCount = results.totalItemCount
        notifyDataSetChanged()
    }
}
