package com.example.praisewhale.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.praisewhale.InfoActivity
import com.example.praisewhale.InfoUserActivity
import com.example.praisewhale.R
import com.example.praisewhale.databinding.FragmentAgreeBinding
import com.example.praisewhale.util.Vibrate

class AgreeFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAgreeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_agree, container, false)
        binding.signUpViewModel = signUpViewModel
        binding.lifecycleOwner = this@AgreeFragment

        setNextButtonClick(binding)
        startInfoActivity(binding)
        startInfoUserActivity(binding)
        return binding.root
    }

    private fun startInfoActivity(binding: FragmentAgreeBinding) {
        binding.layoutPersonalInformationAgree.setOnClickListener {
            startActivity(Intent(requireContext(), InfoActivity::class.java))
        }
    }

    private fun startInfoUserActivity(binding: FragmentAgreeBinding) {
        binding.layoutServiceAgree.setOnClickListener {
            startActivity(Intent(requireContext(), InfoUserActivity::class.java))
        }
    }

    private fun setNextButtonClick(binding: FragmentAgreeBinding) {
        binding.btnAgreeNext.setOnClickListener {
            if (signUpViewModel.isAgree.value!!) {
                (activity as SignUpActivity).replaceFragment(UserNameFragment())
            } else {
                Vibrate.startVibrate(requireContext())
            }
        }
    }
}