package com.drawer.platform.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.drawer.platform.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slides = listOf(
            OnboardingSlide("Welcome to DrawerWide", "Your all-in-one marketplace. Buy, sell, and deliver — everything in one place.", "\uD83D\uDECD\uFE0F"),
            OnboardingSlide("Three Powerful Modes", "Open your own store as a Seller, shop as a Buyer, or earn by delivering as a Partner.", "\uD83D\uDE80"),
            OnboardingSlide("Start Your Journey", "Create your account, choose your role, and join thousands already using DrawerWide.", "✨")
        )

        val adapter = OnboardingPagerAdapter(slides)
        binding.viewPager.adapter = adapter
        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnNext.text = if (position == slides.lastIndex) "Choose Your Role" else "Next"
                binding.btnSkip.visibility = if (position == slides.lastIndex)
                    android.view.View.INVISIBLE else android.view.View.VISIBLE
            }
        })

        binding.btnNext.setOnClickListener {
            val cur = binding.viewPager.currentItem
            if (cur < slides.lastIndex) binding.viewPager.currentItem = cur + 1
            else goToModeSelection()
        }
        binding.btnSkip.setOnClickListener { goToModeSelection() }
    }

    private fun goToModeSelection() {
        startActivity(Intent(this, ModeSelectionActivity::class.java))
        finish()
    }
}

data class OnboardingSlide(val title: String, val description: String, val emoji: String)
