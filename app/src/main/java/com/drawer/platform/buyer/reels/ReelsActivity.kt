package com.drawer.platform.buyer.reels

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2
import com.drawer.platform.databinding.ActivityReelsBinding
import com.drawer.platform.utils.hide
import com.drawer.platform.utils.show
import java.io.File

class ReelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReelsBinding
    private val vm: ReelsViewModel by viewModels()
    private lateinit var player: ExoPlayer
    private lateinit var adapter: ReelsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = ExoPlayer.Builder(this).build()
        adapter = ReelsPagerAdapter(emptyList(), player)
        binding.viewPagerReels.adapter = adapter
        binding.viewPagerReels.orientation = ViewPager2.ORIENTATION_VERTICAL

        binding.viewPagerReels.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                playVideoAt(position)
            }
        })

        binding.btnClose.setOnClickListener { finish() }

        vm.reelProducts.observe(this) { products ->
            if (products.isEmpty()) {
                binding.tvNoReels.show()
                binding.viewPagerReels.hide()
            } else {
                binding.tvNoReels.hide()
                binding.viewPagerReels.show()
                adapter.updateData(products)
                playVideoAt(0)
            }
        }
    }

    private fun playVideoAt(position: Int) {
        val path = adapter.getVideoPathAt(position) ?: return
        val file = File(path)
        if (!file.exists()) return
        player.setMediaItem(MediaItem.fromUri(Uri.fromFile(file)))
        player.prepare()
        player.playWhenReady = true
        player.repeatMode = ExoPlayer.REPEAT_MODE_ONE

        // Find the PlayerView in the current page and attach player
        val recycler = binding.viewPagerReels.getChildAt(0) as? androidx.recyclerview.widget.RecyclerView
        recycler?.findViewHolderForAdapterPosition(position)?.let { vh ->
            if (vh is ReelsPagerAdapter.ReelVH) {
                adapter.attachPlayer(vh.binding.playerView)
            }
        }
    }

    override fun onPause() { super.onPause(); player.pause() }
    override fun onResume() { super.onResume(); player.play() }
    override fun onDestroy() { super.onDestroy(); player.release() }
}
