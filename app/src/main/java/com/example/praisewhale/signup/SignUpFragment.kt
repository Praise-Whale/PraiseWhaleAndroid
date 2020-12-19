package com.example.praisewhale.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.praisewhale.MainActivity
import com.example.praisewhale.R
import com.example.praisewhale.databinding.FragmentSignUpBinding
import com.example.praisewhale.databinding.FragmentUserNameBinding

class SignUpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding : FragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.lifecycleOwner = this

        setNextClickListener(binding)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val imm = requireView().context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setNextClickListener(binding : FragmentSignUpBinding) {
        binding.btnNext.setOnClickListener{
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}