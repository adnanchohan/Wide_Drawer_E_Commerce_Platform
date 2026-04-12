package com.drawer.platform.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.drawer.platform.R
import com.drawer.platform.databinding.ActivityAuthBinding
import com.drawer.platform.utils.Constants

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    lateinit var selectedMode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedMode = intent.getStringExtra(Constants.EXTRA_MODE) ?: Constants.MODE_BUYER

        val badgeRes = when (selectedMode) {
            Constants.MODE_SELLER -> "\uD83C\uDFEA Seller Account"
            Constants.MODE_DELIVER -> "\uD83D\uDE9A Delivery Partner"
            else -> "\uD83D\uDED2 Buyer Account"
        }
        binding.tvModeLabel.text = badgeRes

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_auth) as NavHostFragment
        // navController is managed by nav_auth.xml automatically
    }
}
