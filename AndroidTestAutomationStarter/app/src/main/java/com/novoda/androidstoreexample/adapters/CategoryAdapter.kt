package com.novoda.androidstoreexample.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.models.Category
import com.novoda.androidstoreexample.utilities.ImageHelper

class CategoryAdapter(
    private val context: Context,
    private val categories: List<Category>,
    private val itemClicked: (Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.category_item, parent, false)
        return Holder(view, itemClicked)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindCategory(categories[position], context)
    }

    inner class Holder(itemView: View, private val itemClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val categoryImage = itemView.findViewById<ImageView>(R.id.categoryImage)
        val categoryName = itemView.findViewById<TextView>(R.id.categoryName)

        fun bindCategory(category: Category, context: Context) {
            val resourceId = ImageHelper().getResourceIdForImage(context, category.image)
            categoryImage?.setImageResource(resourceId)
            categoryName?.text = category.title
            itemView.setOnClickListener { itemClick(category.id) }
        }
    }
}