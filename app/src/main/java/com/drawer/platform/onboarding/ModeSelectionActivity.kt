package com.drawer.platform.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drawer.platform.auth.AuthActivity
import com.drawer.platform.databinding.ActivityModeSelectionBinding
import com.drawer.platform.utils.Constants

class ModeSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModeSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardSeller.setOnClickListener { openAuth(Constants.MODE_SELLER) }
        binding.cardBuyer.setOnClickListener { openAuth(Constants.MODE_BUYER) }
        binding.cardDeliver.setOnClickListener { openAuth(Constants.MODE_DELIVER) }
    }

    private fun openAuth(mode: String) {
        startActivity(Intent(this, AuthActivity::class.java).putExtra(Constants.EXTRA_MODE, mode))
    }
}
