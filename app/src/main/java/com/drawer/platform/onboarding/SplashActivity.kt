package com.drawer.platform.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.deliver.orders.DeliverOrdersActivity
import com.drawer.platform.seller.dashboard.SellerDashboardActivity
import com.drawer.platform.ui.theme.DrawerWideTheme
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            val prefs = SharedPrefManager.getInstance(this)
//            DrawerWideTheme(darkTheme = prefs.isDarkMode()) {
//                SplashScreen { navigateToNext() }
//            }
//        }
        navigateToNext()
    }

    @Composable
    fun SplashScreen(onTimeout: () -> Unit) {
        val alphaAnim = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            alphaAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500)
            )
            delay(500)
            onTimeout()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(alphaAnim.value)
            ) {
                Text(
                    text = "✨",
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "E-Baazar",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Buy · Sell",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }

    private fun navigateToNext() {
        val prefs = SharedPrefManager.getInstance(this)
        val intent = if (prefs.isLoggedIn()) {
            when (prefs.getUserMode()) {
                Constants.MODE_SELLER -> Intent(this, SellerDashboardActivity::class.java)
                Constants.MODE_DELIVER -> Intent(this, DeliverOrdersActivity::class.java)
                else -> Intent(this, BuyerHomeActivity::class.java)
            }
        } else {
            if (prefs.isOnboardingFinished()) {
                Intent(this, BuyerHomeActivity::class.java)
            } else {
                Intent(this, OnboardingActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }
}
