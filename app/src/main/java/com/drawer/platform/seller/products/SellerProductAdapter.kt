package com.drawer.platform.seller.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.databinding.ItemProductSellerBinding
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.loadFromPath
import com.drawer.platform.utils.toPrice

class SellerProductAdapter(private val onClick: (ProductEntity) -> Unit) :
    ListAdapter<ProductEntity, SellerProductAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemProductSellerBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: ProductEntity) {
            b.tvProductName.text = p.name
            b.tvProductPrice.text = p.price.toPrice()
            b.tvProductCategory.text = p.category
            b.tvStock.text = "Stock: ${p.stock}"
            val firstImage = FileHelper.getFirstImagePath(p.imagePaths)
            b.ivProductImage.loadFromPath(firstImage)
            b.root.setOnClickListener { onClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemProductSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ProductEntity>() {
            override fun areItemsTheSame(a: ProductEntity, b: ProductEntity) = a.id == b.id
            override fun areContentsTheSame(a: ProductEntity, b: ProductEntity) = a == b
        }
    }
}
