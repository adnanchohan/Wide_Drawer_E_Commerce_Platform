package com.drawer.platform.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.launch

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = SharedPrefManager.getInstance(this)
            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
                OnboardingScreen { goToBuyerHome() }
            }
        }
    }

    @Composable
    fun OnboardingScreen(onFinish: () -> Unit) {
        val slides = listOf(
            OnboardingSlide("Welcome to DrawerWide", "Your all-in-one marketplace. Buy and sell everything in one place.", "\uD83D\uDECD\uFE0F"),
            OnboardingSlide("Seamless Shopping", "Explore amazing products from local sellers and enjoy a premium experience.", "\uD83D\uDE80"),
            OnboardingSlide("Start Your Journey", "Create your account, shop your favorites, or even start your own store.", "✨")
        )

        val pagerState = rememberPagerState(pageCount = { slides.size })
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = onFinish,
                modifier = Modifier.align(Alignment.End),
                enabled = pagerState.currentPage < slides.lastIndex
            ) {
                Text("Skip", color = MaterialTheme.colorScheme.primary)
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                SlideContent(slides[page])
            }

            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(slides.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < slides.lastIndex) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (pagerState.currentPage == slides.lastIndex) "Get Started" else "Next")
            }
        }
    }

    @Composable
    fun SlideContent(slide: OnboardingSlide) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = slide.emoji, fontSize = 100.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = slide.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = slide.description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }

    private fun goToBuyerHome() {
        SharedPrefManager.getInstance(this).setOnboardingFinished(true)
        startActivity(Intent(this, BuyerHomeActivity::class.java))
        finish()
    }
}
