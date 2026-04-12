package com.drawer.platform.buyer.reels

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.drawer.platform.buyer.product.ProductDetailActivity
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.databinding.ItemReelBinding
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.toPrice
import java.io.File

class ReelsPagerAdapter(
    private var products: List<ProductEntity>,
    private val player: ExoPlayer
) : RecyclerView.Adapter<ReelsPagerAdapter.ReelVH>() {

    private var activePlayerView: PlayerView? = null

    inner class ReelVH(val binding: ItemReelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.tvReelProductName.text = product.name
            binding.tvReelPrice.text = product.price.toPrice()
            binding.tvReelCategory.text = product.category
            binding.btnViewProduct.setOnClickListener {
                val ctx = binding.root.context
                ctx.startActivity(Intent(ctx, ProductDetailActivity::class.java)
                    .putExtra(Constants.EXTRA_PRODUCT_ID, product.id))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReelVH(
        ItemReelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ReelVH, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun playAt(position: Int) {
        if (position < 0 || position >= products.size) return
        val product = products[position]
        val videoPath = product.videoPath ?: return
        val file = File(videoPath)
        if (!file.exists()) return

        activePlayerView?.player = null
        // Player attachment happens in ReelsActivity after this call
    }

    fun getVideoPathAt(position: Int): String? =
        if (position in products.indices) products[position].videoPath else null

    fun attachPlayer(playerView: PlayerView) {
        activePlayerView = playerView
        playerView.player = player
    }

    fun updateData(newProducts: List<ProductEntity>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
