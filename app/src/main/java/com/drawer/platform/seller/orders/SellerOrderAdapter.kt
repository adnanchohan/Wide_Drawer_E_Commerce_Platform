package com.drawer.platform.seller.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.data.model.OrderEntity
import com.drawer.platform.databinding.ItemSellerOrderBinding
import com.drawer.platform.utils.toPrice
import com.drawer.platform.utils.toStatusDisplay

class SellerOrderAdapter(
    private val onAccept: (OrderEntity) -> Unit,
    private val onCancel: (OrderEntity) -> Unit
) : ListAdapter<OrderEntity, SellerOrderAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemSellerOrderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(order: OrderEntity) {
            b.tvOrderId.text = "Order #${order.id}"
            b.tvProductName.text = order.productName
            b.tvPrice.text = (order.productPrice * order.quantity).toPrice()
            b.tvQuantity.text = "Qty: ${order.quantity}"
            b.tvStatus.text = order.status.toStatusDisplay()
            b.tvBuyerName.text = "Buyer: ${order.buyerName}"
            b.tvBuyerAddress.text = order.buyerAddress
            if (order.status == "PENDING") {
                b.btnAccept.isEnabled = true
                b.btnCancel.isEnabled = true
            } else {
                b.btnAccept.isEnabled = false
                b.btnCancel.isEnabled = false
            }
            b.btnAccept.setOnClickListener { onAccept(order) }
            b.btnCancel.setOnClickListener { onCancel(order) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemSellerOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, pos: Int) = holder.bind(getItem(pos))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<OrderEntity>() {
            override fun areItemsTheSame(a: OrderEntity, b: OrderEntity) = a.id == b.id
            override fun areContentsTheSame(a: OrderEntity, b: OrderEntity) = a == b
        }
    }
}
