package com.sopt27.praisewhale.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sopt27.praisewhale.R
import com.sopt27.praisewhale.databinding.ActivityOnBoardingBinding
import com.sopt27.praisewhale.signup.SignUpActivity
import com.sopt27.praisewhale.util.MyApplication
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
                MyApplication.mySharedPreferences.setBooleanValue("onBoarding", true)
                startActivity(Intent(this@OnBoardingActivity, SignUpActivity::class.java))
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