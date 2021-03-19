package com.sopt27.praisewhale.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sopt27.praisewhale.R
import com.sopt27.praisewhale.databinding.FragmentAgreeBinding
import com.sopt27.praisewhale.util.Vibrate

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
        startPersonalInformationAgree(binding)
        startServiceAgreeIntent(binding)
        return binding.root
    }

    private fun startPersonalInformationAgree(binding: FragmentAgreeBinding) {
        binding.layoutPersonalInformationAgree.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/4ae478f551f249d097a6e46cffad6d07")
            )
            startActivity(intent)
        }
    }

    private fun startServiceAgreeIntent(binding: FragmentAgreeBinding) {
        binding.layoutServiceAgree.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.notion.so/8ced90e384b1417ab6e24ce9c8436ab8")
            )
            startActivity(intent)
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