package com.drawer.platform.buyer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.databinding.ItemProductCardBinding
import com.drawer.platform.utils.FileHelper
import com.drawer.platform.utils.loadFromPath
import com.drawer.platform.utils.toPrice

class BuyerProductCardAdapter(private val onClick: (ProductEntity) -> Unit) :
    ListAdapter<ProductEntity, BuyerProductCardAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemProductCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: ProductEntity) {
            b.tvProductName.text = p.name
            b.tvProductPrice.text = p.price.toPrice()
            b.tvCategory.text = p.category
            val img = FileHelper.getFirstImagePath(p.imagePaths)
            b.ivProduct.loadFromPath(img)
            if (p.videoPath != null) b.ivPlayBadge.visibility = android.view.View.VISIBLE
            else b.ivPlayBadge.visibility = android.view.View.GONE
            b.root.setOnClickListener { onClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ProductEntity>() {
            override fun areItemsTheSame(a: ProductEntity, b: ProductEntity) = a.id == b.id
            override fun areContentsTheSame(a: ProductEntity, b: ProductEntity) = a == b
        }
    }
}
