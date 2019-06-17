package com.novoda.androidstoreexample.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.listener.BasketAdapterListener
import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.utilities.ImageHelper

class BasketAdapter(
    private val context: Context,
    private val orders: List<Order>,
    private val listener: BasketAdapterListener
) : RecyclerView.Adapter<BasketAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.basket_item, parent, false)
        return Holder(view, listener)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindProducts(orders[position], context)
    }

    inner class Holder(itemView: View, private val productListener: BasketAdapterListener) : RecyclerView.ViewHolder(itemView) {
        private val productImage = itemView.findViewById<ImageView>(R.id.basketProductImage)
        private val productTitle = itemView.findViewById<TextView>(R.id.basketProductTitle)
        private val plusButton = itemView.findViewById<Button>(R.id.basket_view_increase_button)
        private val minusButton = itemView.findViewById<Button>(R.id.basket_view_decrease_button)
        private val numberOfProducts = itemView.findViewById<TextView>(R.id.number_of_items_text_field)
        private val priceField = itemView.findViewById<TextView>(R.id.price_per_item_textview)
        private val totalField = itemView.findViewById<TextView>(R.id.total_text_view)

        fun bindProducts(order: Order, context: Context) {
            with(order) {
                val resourceId: Int = ImageHelper().getResourceIdForImage(context, product.image)
                productImage?.setImageResource(resourceId)
                productImage?.setOnClickListener{
                    productListener.onProductImageClicked(product)
                }
                plusButton?.setOnClickListener{
                    productListener.onIncreaseAmountClicked(product)
                }
                minusButton?.setOnClickListener{
                    productListener.onDecreaseAmountClicked(product)
                }
                priceField?.text = context.getString(R.string.price_template, product.price)
                totalField?.text = context.getString(R.string.price_template, amount)
                productTitle?.text = order.product.title
                numberOfProducts?.text = order.numberOfItems.toString()
            }
        }
    }
}