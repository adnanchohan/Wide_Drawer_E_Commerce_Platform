package com.drawer.platform.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.databinding.ActivitySplashBinding
import com.drawer.platform.deliver.orders.DeliverOrdersActivity
import com.drawer.platform.seller.dashboard.SellerDashboardActivity
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeSlideUp = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.tvLogo.startAnimation(fadeSlideUp)
        binding.tvAppName.startAnimation(fadeSlideUp)
        binding.tvTagline.startAnimation(fadeSlideUp)

        Handler(Looper.getMainLooper()).postDelayed({ navigateToNext() }, 2500)
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
            Intent(this, OnboardingActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
