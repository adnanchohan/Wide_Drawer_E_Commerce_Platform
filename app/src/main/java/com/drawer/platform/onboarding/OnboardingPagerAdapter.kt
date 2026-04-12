package com.drawer.platform.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drawer.platform.databinding.FragmentOnboardingSlideBinding

class OnboardingPagerAdapter(private val slides: List<OnboardingSlide>) :
    RecyclerView.Adapter<OnboardingPagerAdapter.SlideViewHolder>() {

    inner class SlideViewHolder(private val binding: FragmentOnboardingSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(slide: OnboardingSlide) {
            binding.tvEmoji.text = slide.emoji
            binding.tvTitle.text = slide.title
            binding.tvDescription.text = slide.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SlideViewHolder(
        FragmentOnboardingSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) = holder.bind(slides[position])
    override fun getItemCount() = slides.size
}
