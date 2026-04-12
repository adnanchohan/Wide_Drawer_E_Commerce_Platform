package com.drawer.platform.buyer.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.drawer.platform.R
import com.drawer.platform.databinding.ActivityBuyerHomeBinding

class BuyerHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyerHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_buyer) as NavHostFragment
        binding.bottomNavBuyer.setupWithNavController(navHost.navController)
    }
}
