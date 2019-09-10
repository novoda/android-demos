package com.novoda.movies.mvi.search.presentation

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.novoda.movies.mvi.search.R
import kotlinx.android.synthetic.main.item_search_result.view.*
import java.net.URL

internal class SearchResultItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val image = view.result_item_image
    private val name = view.result_item_name

    @SuppressLint("SetTextI18n")
    fun bind(item: ViewSearchItem) {
        image.clipToOutline = true
        image.loadImage(
                item.thumbnailUrl,
                {
                    //no-op
                },
                {
                    Log.e(TAG, "Could not download image with url ${item.thumbnailUrl}")
                }
        )
        name.text = item.name
    }

    companion object {
        var TAG: String = SearchResultItemViewHolder::class.java.simpleName

        fun create(parent: ViewGroup): SearchResultItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_result, parent, false)
            return SearchResultItemViewHolder(view)
        }
    }
}

fun ImageView.loadImage(url: URL, onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit) {
    val simpleGlideListener = SimpleGlideListener(
        onSuccess
    ) { throwable ->
        onError(throwable ?: Error(url.toString()))
    }
    Glide.with(this)
        .load(GlideUrl(url))
        .listener(simpleGlideListener)
        .into(this)
}

class SimpleGlideListener(
    private val onSuccess: () -> Unit,
    private val onFailure: (Throwable?) -> Unit
) : RequestListener<Drawable> {

    override fun onLoadFailed(
        exception: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        onFailure(exception)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        onSuccess()
        return false
    }
}
