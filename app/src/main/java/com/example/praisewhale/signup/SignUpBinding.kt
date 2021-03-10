package com.example.praisewhale.signup

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.praisewhale.R

object SignUpBinding {
    @BindingAdapter("visibleResetButton")
    @JvmStatic
    fun visibleResetButton(btnReset: View, name: String) {
        btnReset.visibility = if (name.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    @BindingAdapter("nickNameCount")
    @JvmStatic
    fun nickNameCount(tvCount: TextView, name: String) {
        tvCount.text = name.length.toString()
    }

    @BindingAdapter("nickNameColor")
    @JvmStatic
    fun nickNameColor(tvCount: TextView, name: String) {
        if (name.isEmpty()) {
            tvCount.setTextColor(tvCount.context.getColor(R.color.brown_grey))
        } else {
            tvCount.setTextColor(tvCount.context.getColor(R.color.brown))
        }
    }

    @BindingAdapter("emptyText", "userNameValid")
    @JvmStatic
    fun userNameValid(btnNext: Button, name: String, isValid: Boolean) {
        if (name.isEmpty() || !isValid) {
            btnNext.setBackgroundResource(R.drawable.btn_next)
        } else {
            btnNext.setBackgroundResource(R.drawable.btn_next_actived)
        }
    }

    @BindingAdapter("whaleNameValid")
    @JvmStatic
    fun whaleNameValid(btnNext: Button, name: String) {
        if (name.isEmpty()) {
            btnNext.setBackgroundResource(R.drawable.btn_next)
        } else {
            btnNext.setBackgroundResource(R.drawable.btn_next_actived)
        }
    }

    @BindingAdapter("allAgreeCheck")
    @JvmStatic
    fun allAgreeCheck(btnAllAgree: ImageButton, agree: Boolean) {
        if (agree) {
            btnAllAgree.setBackgroundResource(R.drawable.agree_ic_checkbox)
        } else {
            btnAllAgree.setBackgroundResource(R.drawable.agree_ic_checkbox_grey)
        }
    }

    @BindingAdapter("agreeCheck")
    @JvmStatic
    fun agreeCheck(imgAgree: ImageView, agree: Boolean) {
        if (agree) {
            imgAgree.setBackgroundResource(R.drawable.agree_ic_check)
        } else {
            imgAgree.setBackgroundResource(R.drawable.agree_ic_check_grey)
        }
    }

    @BindingAdapter("agreeNextButton")
    @JvmStatic
    fun agreeNextButton(btnNext: Button, agree: Boolean) {
        if (agree) {
            btnNext.setBackgroundResource(R.drawable.btn_next_actived)
        } else {
            btnNext.setBackgroundResource(R.drawable.btn_next)
        }
    }
}
