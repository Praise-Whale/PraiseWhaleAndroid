package com.example.praisewhale.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.praisewhale.CollectionImpl
import com.example.praisewhale.R
import com.example.praisewhale.data.RequestSignUp
import com.example.praisewhale.data.ResponseToken
import com.example.praisewhale.databinding.FragmentWhaleNameBinding
import com.example.praisewhale.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WhaleNameFragment : Fragment() {

    private val signUpViewModel : SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentWhaleNameBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_whale_name, container, false)
        binding.signUpViewModel = signUpViewModel
        binding.lifecycleOwner = this

        setFocus(binding)
        setFocusListener(binding)
        setBackClickListener(binding)
        setNextClickListener(binding)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val imm = requireView().context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun setFocus(binding : FragmentWhaleNameBinding) {
        binding.etWhaleName.requestFocus()
    }

    private fun setFocusListener(binding : FragmentWhaleNameBinding) {
        binding.etWhaleName.setOnFocusChangeListener { _, isFocused ->
            if(isFocused)  binding.etWhaleName.setBackgroundResource(R.drawable.background_edit_text_focused)
            else binding.etWhaleName.setBackgroundResource(R.drawable.background_edit_text)
        }
    }

    private fun setBackClickListener(binding : FragmentWhaleNameBinding) {
        binding.btnBack.setOnClickListener {
            (activity as SignUpActivity).finishFragment(this)
        }
    }

    private fun setNextClickListener(binding : FragmentWhaleNameBinding) {
        binding.btnCheck.setOnClickListener {
            if(!(signUpViewModel.userName.value.isNullOrEmpty())) signUp()
        }
    }

    private fun signUp() {
        val body = RequestSignUp(nickName = signUpViewModel.userName.value!!, whaleName = signUpViewModel.whaleName.value!!)
        val call: Call<ResponseToken> = CollectionImpl.service.signUp(body)
        call.enqueue(object : Callback<ResponseToken> {
            override fun onFailure(call: Call<ResponseToken>, t: Throwable) {
                Log.d("response", t.localizedMessage!!)
            }
            override fun onResponse(
                call: Call<ResponseToken>,
                response: Response<ResponseToken>
            ) {
                Log.d("response", response.body().toString())
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        MyApplication.mySharedPreferences.setValue("nickName", body.nickName)
                        MyApplication.mySharedPreferences.setValue("whaleName", body.whaleName)
                        MyApplication.mySharedPreferences.setValue("token", it.data.accessToken)
                        (activity as SignUpActivity).replaceFragment(SignUpFragment())
                    } ?: Toast.makeText(view!!.context, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}