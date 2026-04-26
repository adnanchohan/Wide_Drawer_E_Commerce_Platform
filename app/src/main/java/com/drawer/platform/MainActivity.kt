package com.drawer.platform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drawer.platform.buyer.home.BuyerHomeActivity
import com.drawer.platform.deliver.orders.DeliverOrdersActivity
import com.drawer.platform.onboarding.OnboardingActivity
import com.drawer.platform.onboarding.SplashActivity
import com.drawer.platform.seller.dashboard.SellerDashboardActivity
import com.drawer.platform.utils.Constants
import com.drawer.platform.utils.SharedPrefManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToNext()
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