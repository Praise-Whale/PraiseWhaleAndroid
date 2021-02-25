package com.example.praisewhale.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.praisewhale.R
import com.example.praisewhale.databinding.FragmentSecondOnBoardingBinding

class SecondOnBoardingFragment : Fragment() {
    private lateinit var binding: FragmentSecondOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_second_on_boarding,
                container,
                false
            )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.imgSecondOnBoarding.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.on_boarding_animation
            )
        )
    }
}