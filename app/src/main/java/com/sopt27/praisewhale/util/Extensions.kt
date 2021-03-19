package com.sopt27.praisewhale.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.sopt27.praisewhale.databinding.CustomToastBinding


fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun TextView.setContextCompatTextColor(color: Int) {
    setTextColor(ContextCompat.getColor(this.context, color))
}

fun View.setContextCompatBackgroundTintList(color: Int) {
    backgroundTintList = ContextCompat.getColorStateList(this.context, color)
}

fun View.fadeIn() {
    if (visibility == View.INVISIBLE) {
        visibility = View.VISIBLE
        alpha = 0f
        animate()
            .alpha(1f)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .setListener(null)
    }
}

fun View.fadeOut() {
    if (visibility == View.VISIBLE) {
        alpha = 1f
        animate()
            .alpha(0f)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = View.INVISIBLE
                }
            })
    }
}

fun Context.showKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun Context.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
        InputMethodManager.HIDE_IMPLICIT_ONLY,
        0
    )
}

fun Context.showToast(text: String) {
    val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val toastViewBinding = CustomToastBinding.inflate(layoutInflater)
    Toast(this).apply {
        view = toastViewBinding.constraintLayoutToastContainer
        toastViewBinding.textViewToastMessage.text = text
        duration = Toast.LENGTH_SHORT
        setGravity(Gravity.BOTTOM, 0, 0)
        show()
    }
}