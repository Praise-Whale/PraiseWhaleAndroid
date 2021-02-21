package com.example.praisewhale.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.praisewhale.R
import com.example.praisewhale.databinding.ActivityOnBoardingBinding
import com.example.praisewhale.signup.SignUpActivity
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
        binding.vpOnBoarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setButtonText(binding, position)
                setButtonClick(binding, position)
                setBackgroundImg(binding, position)
                setButtonAnimation(binding)
            }
        })
    }

    private fun setButtonText(binding: ActivityOnBoardingBinding, position: Int) {
        if (position == 3) {
            binding.btnOnBoardingNext.text = getString(R.string.start)
        } else {
            binding.btnOnBoardingNext.text = getString(R.string.next)
        }
    }

    private fun setButtonClick(binding: ActivityOnBoardingBinding, position: Int) {
        binding.btnOnBoardingNext.setOnClickListener {
            if (position == 3) {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.vpOnBoarding.currentItem += 1
            }
        }
    }

    private fun setBackgroundImg(binding: ActivityOnBoardingBinding, position: Int) {
        if (position == 3) {
            binding.layoutOnBoarding.setBackgroundResource(R.drawable.onboarding_4_bg)
        } else {
            binding.layoutOnBoarding.setBackgroundResource(R.drawable.onboarding_bg)
        }
    }

    private fun setButtonAnimation(binding: ActivityOnBoardingBinding) {
        binding.btnOnBoardingNext.alpha = 0f
        binding.btnOnBoardingNext.animate().apply {
            duration = 2000
            alpha(1f)
        }.start()
    }
}