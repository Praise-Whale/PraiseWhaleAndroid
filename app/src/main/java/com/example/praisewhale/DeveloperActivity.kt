package com.example.praisewhale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_developer.*

class DeveloperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)

<<<<<<< HEAD
        developer_close_btn.setOnClickListener {
            finish()
        }

=======
        developer_close_btn.setOnClickListener{
            finish()
        }
>>>>>>> 749fe9ffab338f2b2e153c5f2079638571965655
    }
}