package com.drawer.platform.buyer.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.data.model.CartItemEntity
import com.drawer.platform.databinding.ItemCartBinding
import com.drawer.platform.utils.loadFromPath
import com.drawer.platform.utils.toPrice

class CartItemAdapter(
    private val onIncrease: (CartItemEntity) -> Unit,
    private val onDecrease: (CartItemEntity) -> Unit,
    private val onRemove: (CartItemEntity) -> Unit
) : ListAdapter<CartItemEntity, CartItemAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: CartItemEntity) {
            b.tvProductName.text = item.productName
            b.tvPrice.text = (item.productPrice * item.quantity).toPrice()
            b.tvStoreName.text = item.storeName
            b.tvQty.text = item.quantity.toString()
            b.ivProduct.loadFromPath(item.productImagePath.ifBlank { null })
            b.btnIncrease.setOnClickListener { onIncrease(item) }
            b.btnDecrease.setOnClickListener { onDecrease(item) }
            b.btnRemove.setOnClickListener { onRemove(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<CartItemEntity>() {
            override fun areItemsTheSame(a: CartItemEntity, b: CartItemEntity) = a.id == b.id
            override fun areContentsTheSame(a: CartItemEntity, b: CartItemEntity) = a == b
        }
    }
}
