package com.example.praisewhale.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.praisewhale.R
import com.example.praisewhale.databinding.ActivityOnBoardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityOnBoardingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        binding.lifecycleOwner = this@OnBoardingActivity

        setViewPagerAdapter(binding)
        setIndicator(binding)
        setViewPagerCallBack(binding)
    }

    private fun setViewPagerAdapter(binding: ActivityOnBoardingBinding) {
        binding.vpOnBoarding.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 4
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> FirstOnBoardingFragment()
                    1 -> SecondOnBoardingFragment()
                    2 -> ThirdOnBoardingFragment()
                    3 -> LastOnBoardingFragment()
                    else -> throw IndexOutOfBoundsException()
                }
            }
        }
    }

    private fun setIndicator(binding: ActivityOnBoardingBinding) {
        TabLayoutMediator(binding.tabOnBoarding, binding.vpOnBoarding) { tab, position -> }.attach()
    }

    private fun setViewPagerCallBack(binding: ActivityOnBoardingBinding) {
        binding.vpOnBoarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 3) {
                    binding.btnOnBoardingNext.text = getString(R.string.start)
                    binding.layoutOnBoarding.setBackgroundResource(R.drawable.onboarding_4_bg)
                } else {
                    binding.btnOnBoardingNext.text = getString(R.string.next)
                    binding.layoutOnBoarding.setBackgroundResource(R.drawable.onboarding_bg)
                }
                binding.btnOnBoardingNext.alpha = 0f
                binding.btnOnBoardingNext.animate().apply {
                    duration = 2000
                    alpha(1f)
                }.start()
            }
        })
    }
}