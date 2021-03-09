package com.example.praisewhale.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat


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