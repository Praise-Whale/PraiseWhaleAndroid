package com.example.praisewhale.signup

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.praisewhale.R

object SignUpBinding {
    @BindingAdapter("visibleResetButton")
    @JvmStatic
    fun visibleResetButton(btnReset : View, name : String) {
        btnReset.visibility = if (name.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    @BindingAdapter("nickNameCount")
    @JvmStatic
    fun nickNameCount(tvCount : TextView, name : String) {
        tvCount.text = name.length.toString()
    }

    @BindingAdapter("nickNameColor")
    @JvmStatic
    fun nickNameColor(tvCount : TextView, name : String) {
        if(name.isEmpty())tvCount.setTextColor(tvCount.context.getColor(R.color.brown_grey))
        else tvCount.setTextColor(tvCount.context.getColor(R.color.brown))
    }

    @BindingAdapter("validation")
    @JvmStatic
    fun validation(btnNext : Button, name : String) {
        if(name.isEmpty()) btnNext.setBackgroundResource(R.drawable.btn_next)
        else btnNext.setBackgroundResource(R.drawable.btn_next_actived)
    }
}