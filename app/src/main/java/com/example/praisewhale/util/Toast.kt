package com.example.praisewhale.util

import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.example.praisewhale.R
import android.widget.Toast


object Toast {
    fun customToast(msg: String, context: Activity) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.toast_custom_layout, context.findViewById(R.id.toast_custom))
        Toast(context).apply {
            layout.findViewById<TextView>(R.id.textView4).text = msg
            view = layout
            setGravity(Gravity.BOTTOM, 0, 100)
            duration = Toast.LENGTH_SHORT
            show()

        }

    }
}