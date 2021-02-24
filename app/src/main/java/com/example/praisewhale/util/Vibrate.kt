package com.example.praisewhale.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

object Vibrate {

    fun startVibrate(context: Context) {
        val vibrator: Vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        Log.d("vibrate", vibrator.hasVibrator().toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }
}