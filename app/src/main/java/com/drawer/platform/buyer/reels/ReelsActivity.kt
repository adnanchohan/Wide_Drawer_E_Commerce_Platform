package com.drawer.platform.buyer.reels

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.drawer.platform.data.model.ProductEntity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.SharedPrefManager
import java.io.File

class ReelsActivity : ComponentActivity() {

    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = ExoPlayer.Builder(this).build()

        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                ReelsScreen(player) { finish() }
            }
        }
    }

    @Composable
    fun ReelsScreen(player: ExoPlayer, onClose: () -> Unit) {
        val vm: ReelsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
        val reelsState = vm.reelProducts.observeAsState(emptyList<ProductEntity>())
        val reels = reelsState.value
        val pagerState = rememberPagerState(pageCount = { reels.size })

        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            if (reels.isEmpty()) {
                Text("No reels available", color = Color.White, modifier = Modifier.align(Alignment.Center))
            } else {
                VerticalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                    val reel = reels[page]
                    ReelItem(reel.videoPath, player, isActive = pagerState.currentPage == page)
                    
                    // Overlay info
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)
                    ) {
                        Text(reel.name, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(reel.category, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }

            IconButton(onClick = onClose, modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }
    }

    @Composable
    fun ReelItem(videoPath: String?, player: ExoPlayer, isActive: Boolean) {
        val context = LocalContext.current
        
        LaunchedEffect(isActive, videoPath) {
            if (isActive && videoPath != null) {
                val file = File(videoPath)
                if (file.exists()) {
                    player.setMediaItem(MediaItem.fromUri(Uri.fromFile(file)))
                    player.prepare()
                    player.playWhenReady = true
                    player.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                }
            }
        }

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    override fun onPause() { super.onPause(); player.pause() }
    override fun onResume() { super.onResume(); player.play() }
    override fun onDestroy() { super.onDestroy(); player.release() }
}
