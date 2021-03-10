package com.example.praisewhale.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.data.ResponseData
import com.example.praisewhale.databinding.FragmentUserNameBinding
import com.example.praisewhale.util.Vibrate
import com.example.praisewhale.util.textChangedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserNameFragment : Fragment() {

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentUserNameBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_name, container, false)
        binding.signUpViewModel = signUpViewModel
        binding.lifecycleOwner = this

        setFocus(binding)
        setBackgroundChangeListener(binding)
        setBackClickListener(binding)
        setErrorObserver(binding)
        setNextClickListener(binding)
        setTextChangedListener(binding)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val imm =
            requireView().context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun setFocus(binding: FragmentUserNameBinding) {
        binding.etNickname.requestFocus()
    }

    private fun setBackClickListener(binding: FragmentUserNameBinding) {
        binding.btnBack.setOnClickListener {
            (activity as SignUpActivity).finishFragment(this)
        }
    }

    private fun setNextClickListener(binding: FragmentUserNameBinding) {
        binding.btnCheck.setOnClickListener {
            if (!(signUpViewModel.userName.value.isNullOrEmpty()) && signUpViewModel.isValid.value!!) {
                nicknameCheck()
            } else {
                Vibrate.startVibrate(requireContext())
            }
        }
    }

    private fun setErrorObserver(binding: FragmentUserNameBinding) {
        signUpViewModel.isValid.observe(viewLifecycleOwner, Observer { isValid ->
            isValid?.let {
                if (!isValid) binding.etNickname.setBackgroundResource(R.drawable.background_edit_text_warning)
                else binding.etNickname.setBackgroundResource(R.drawable.background_edit_text_focused)
            }
        })
    }

    private fun setBackgroundChangeListener(binding: FragmentUserNameBinding) {
        binding.etNickname.setOnFocusChangeListener { _, isFocused ->
            signUpViewModel.isValid.observe(viewLifecycleOwner, Observer { isValid ->
                isValid?.let {
                    if (!isValid) binding.etNickname.setBackgroundResource(R.drawable.background_edit_text_warning) // nickname error
                    else if (isValid && isFocused) binding.etNickname.setBackgroundResource(R.drawable.background_edit_text_focused) // have focus
                    else if (isValid && !isFocused) binding.etNickname.setBackgroundResource(R.drawable.background_edit_text) // clear focus
                }
            })
        }
    }

    private fun setTextChangedListener(binding: FragmentUserNameBinding) {
        binding.etNickname.textChangedListener {
            if (!(signUpViewModel.isValid.value!!)) signUpViewModel.validReset()
        }
    }

    private fun nicknameError() {
        signUpViewModel.nameFail()
        Vibrate.startVibrate(requireContext())
    }

    private fun nicknameCheck() {
        val call: Call<ResponseData> =
            CollectionImpl.service.nicknameCheck(signUpViewModel.userName.value!!)
        call.enqueue(object : Callback<ResponseData> {
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.d("response", t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ResponseData>,
                response: Response<ResponseData>
            ) {
                Log.d("response", response.body().toString())
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        (activity as SignUpActivity).replaceFragment(WhaleNameFragment())
                    } ?: nicknameError()
            }
        })
    }
}