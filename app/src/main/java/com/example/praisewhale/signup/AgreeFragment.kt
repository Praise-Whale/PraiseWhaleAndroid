package com.example.praisewhale.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.praisewhale.R
import com.example.praisewhale.databinding.FragmentAgreeBinding

class AgreeFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAgreeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_agree, container, false)
        binding.signUpViewModel = signUpViewModel
        binding.lifecycleOwner = this@AgreeFragment
        return binding.root
    }
}