package com.drawer.platform.buyer.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.data.model.OrderEntity
import com.drawer.platform.databinding.ItemBuyerOrderBinding
import com.drawer.platform.utils.loadFromPath
import com.drawer.platform.utils.toPrice
import com.drawer.platform.utils.toStatusDisplay

class BuyerOrderAdapter : ListAdapter<OrderEntity, BuyerOrderAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemBuyerOrderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(order: OrderEntity) {
            b.tvOrderId.text = "Order #${order.id}"
            b.tvProductName.text = order.productName
            b.tvStoreName.text = order.storeName
            b.tvTotal.text = (order.productPrice * order.quantity).toPrice()
            b.tvStatus.text = order.status.toStatusDisplay()
            b.tvQty.text = "Qty: ${order.quantity}"
            b.ivProduct.loadFromPath(order.productImagePath.ifBlank { null })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemBuyerOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<OrderEntity>() {
            override fun areItemsTheSame(a: OrderEntity, b: OrderEntity) = a.id == b.id
            override fun areContentsTheSame(a: OrderEntity, b: OrderEntity) = a == b
        }
    }
}
