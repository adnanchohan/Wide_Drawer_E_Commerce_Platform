package com.drawer.platform.buyer.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.R
import com.drawer.platform.databinding.ItemCategoryChipBinding

class CategoryChipAdapter(
    private val categories: List<String>,
    private val onSelect: (String) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.VH>() {

    private var selectedPos = 0

    inner class VH(private val b: ItemCategoryChipBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(cat: String, isSelected: Boolean) {
            b.tvCategory.text = cat
            if (isSelected) {
                b.tvCategory.setBackgroundResource(R.drawable.bg_chip_selected)
                b.tvCategory.setTextColor(ContextCompat.getColor(b.root.context, R.color.white))
            } else {
                b.tvCategory.setBackgroundResource(R.drawable.bg_chip_normal)
                b.tvCategory.setTextColor(ContextCompat.getColor(b.root.context, R.color.color_on_surface))
            }
            b.root.setOnClickListener {
                val prev = selectedPos
                selectedPos = adapterPosition
                notifyItemChanged(prev)
                notifyItemChanged(selectedPos)
                onSelect(cat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        ItemCategoryChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(categories[position], position == selectedPos)

    override fun getItemCount() = categories.size
}
