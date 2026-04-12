package com.drawer.platform.deliver.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.data.model.OrderEntity
import com.drawer.platform.databinding.ItemDeliverOrderBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show
import com.drawer.platform.utils.toPrice
import com.drawer.platform.utils.toStatusDisplay

class DeliverOrderAdapter(
    private val showAccept: Boolean,
    private val onAccept: (OrderEntity) -> Unit,
    private val onUpdateStatus: (OrderEntity, String) -> Unit
) : ListAdapter<OrderEntity, DeliverOrderAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemDeliverOrderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(order: OrderEntity) {
            b.tvOrderId.text = "Order #${order.id}"
            b.tvProductName.text = order.productName
            b.tvTotal.text = (order.productPrice * order.quantity).toPrice()
            b.tvStatus.text = order.status.toStatusDisplay()
            b.tvPickupAddress.text = "📦 Pickup: ${order.sellerAddress.ifBlank { "Address not provided" }}"
            b.tvDeliveryAddress.text = "🏠 Deliver: ${order.buyerAddress}"
            b.tvBuyerName.text = "Buyer: ${order.buyerName}"

            if (showAccept) {
                b.btnAccept.show()
                b.btnPickedUp.hide()
                b.btnDelivered.hide()
                b.btnAccept.setOnClickListener { onAccept(order) }
            } else {
                b.btnAccept.hide()
                when (order.status) {
                    "ACCEPTED" -> { b.btnPickedUp.show(); b.btnDelivered.hide() }
                    "PICKED_UP" -> { b.btnPickedUp.hide(); b.btnDelivered.show() }
                    else -> { b.btnPickedUp.hide(); b.btnDelivered.hide() }
                }
                b.btnPickedUp.setOnClickListener { onUpdateStatus(order, "PICKED_UP") }
                b.btnDelivered.setOnClickListener { onUpdateStatus(order, "DELIVERED") }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemDeliverOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, pos: Int) = holder.bind(getItem(pos))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<OrderEntity>() {
            override fun areItemsTheSame(a: OrderEntity, b: OrderEntity) = a.id == b.id
            override fun areContentsTheSame(a: OrderEntity, b: OrderEntity) = a == b
        }
    }
}
