package com.novoda.androidstoreexample.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.utilities.ImageHelper

class BasketAdapter(
    private val context: Context,
    private val orders: List<Order>,
    private val itemClicked: (Int) -> Unit
) : RecyclerView.Adapter<BasketAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.basket_item, parent, false)
        return Holder(view, itemClicked)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindProducts(orders[position], context)
    }

    inner class Holder(itemView: View, private val itemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val productImage = itemView.findViewById<ImageView>(R.id.basketProductImage)
        private val productTitle = itemView.findViewById<TextView>(R.id.basketProductTitle)
        private val numberOfProducts = itemView.findViewById<TextView>(R.id.NumberOfItemsTextField)

        fun bindProducts(order: Order, context: Context) {
            val resourceId: Int = ImageHelper().getResourceIdForImage(context, order.product.image)
            productImage?.setImageResource(resourceId)
            productTitle?.text = order.product.title
            numberOfProducts?.text = order.numberOfItems.toString()
        }
    }
}