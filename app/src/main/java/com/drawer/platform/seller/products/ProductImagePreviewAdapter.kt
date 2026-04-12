package com.drawer.platform.seller.products

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.databinding.ItemProductImageBinding
import com.drawer.platform.utils.loadFromUri

class ProductImagePreviewAdapter(private val onRemove: (Uri) -> Unit) :
    ListAdapter<Uri, ProductImagePreviewAdapter.VH>(DIFF) {

    inner class VH(private val b: ItemProductImageBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(uri: Uri) {
            b.ivImage.loadFromUri(uri)
            b.btnRemove.setOnClickListener { onRemove(uri) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(a: Uri, b: Uri) = a == b
            override fun areContentsTheSame(a: Uri, b: Uri) = a == b
        }
    }
}
