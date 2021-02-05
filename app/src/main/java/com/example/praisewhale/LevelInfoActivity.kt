package com.example.praisewhale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_level_info.*

class LevelInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_info)

<<<<<<< HEAD
        btn_back.setOnClickListener {
            finish()
        }

=======
        btn_back.setOnClickListener{
            finish()
        }
>>>>>>> 749fe9ffab338f2b2e153c5f2079638571965655
    }
}