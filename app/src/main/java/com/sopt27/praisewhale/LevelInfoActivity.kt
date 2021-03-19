package com.sopt27.praisewhale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sopt27.praisewhale.databinding.ActivityLevelInfoBinding

class LevelInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLevelInfoBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_level_info)

        setBackClickListener(binding)
    }

    private fun setBackClickListener(binding: ActivityLevelInfoBinding) {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}