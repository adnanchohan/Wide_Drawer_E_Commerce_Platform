package com.drawer.platform.buyer.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.databinding.ItemProductImageGalleryBinding
import com.drawer.platform.utils.loadFromPath

class ProductImageGalleryAdapter :
    ListAdapter<String, ProductImageGalleryAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemProductImageGalleryBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(path: String) { b.ivGalleryImage.loadFromPath(path) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemProductImageGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(a: String, b: String) = a == b
            override fun areContentsTheSame(a: String, b: String) = a == b
        }
    }
}
