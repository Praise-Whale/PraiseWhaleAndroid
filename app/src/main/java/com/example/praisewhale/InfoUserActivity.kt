package com.example.praisewhale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_developer.*
import kotlinx.android.synthetic.main.activity_info_user.*

class InfoUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_user)


        infouser_back.setOnClickListener{
            finish()
        }

    }
}