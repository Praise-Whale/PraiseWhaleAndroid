package com.example.praisewhale

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.praisewhale.collection.ui.CollectionFragment
import com.example.praisewhale.databinding.CustomToastHomeBinding
import com.example.praisewhale.fragment.PraiseLevelFragment
import com.example.praisewhale.home.ui.HomeFragment
import com.example.praisewhale.notification.AlarmReceiver
import com.example.praisewhale.util.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainFragment by lazy { HomeFragment() }
    private val collectionFragment by lazy { CollectionFragment() }
    private val praiseLevelFragment by lazy { PraiseLevelFragment() }
    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initBottomNav()
        notification()
    }

    private fun initBottomNav() {
        bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_main -> {
                        changeFragment(mainFragment)
                    }
                    R.id.menu_collection -> {
                        changeFragment(collectionFragment)
                    }
                    R.id.menu_praiseLevel -> {
                        changeFragment(praiseLevelFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.menu_main
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_fragmentContainer, fragment).commit()
    }

    private fun notification() {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()) {
            val calendar = Calendar.getInstance()
            calendar.set(
                Calendar.HOUR_OF_DAY,
                MyApplication.mySharedPreferences.getValue("alarm_hour", "9").toInt()
            )
            calendar.set(
                Calendar.MINUTE,
                MyApplication.mySharedPreferences.getValue("alarm_minute", "00").toInt()
            )
            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
    
    fun showFinishToast() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        showToast()
        backPressedTime = System.currentTimeMillis()
    }

    private fun showToast() {
        val toastViewBinding = CustomToastHomeBinding.inflate(layoutInflater)
        Toast(this).apply {
            view = toastViewBinding.constraintLayoutToastContainer
            toastViewBinding.textViewToastMessage.text = "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다."
            duration = Toast.LENGTH_SHORT
            setGravity(Gravity.BOTTOM, 0, 0)
            show()
        }
    }
}