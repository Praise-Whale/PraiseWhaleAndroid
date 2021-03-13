package com.example.praisewhale

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.praisewhale.collection.ui.CollectionFragment
import com.example.praisewhale.databinding.ActivityMainBinding
import com.example.praisewhale.fragment.PraiseLevelFragment
import com.example.praisewhale.home.ui.HomeFragment
import com.example.praisewhale.notification.AlarmReceiver
import com.example.praisewhale.util.MyApplication
import com.example.praisewhale.util.showToast
import java.util.*


class MainActivity : AppCompatActivity() {

    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val homeFragment by lazy { HomeFragment() }
    private val collectionFragment by lazy { CollectionFragment() }
    private val praiseLevelFragment by lazy { PraiseLevelFragment() }

    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        initBottomNav()
        notification()
    }

    private fun setViewBinding() {
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun initBottomNav() {
        viewBinding.bottomNavigationView.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    menu.getItem(0).itemId -> showFragmentHome()
                    menu.getItem(1).itemId -> showFragmentCollection()
                    menu.getItem(2).itemId -> showFragmentPraiseLevel()
                }
                true
            }
            selectedItemId = menu.getItem(0).itemId
        }
    }

    private fun showFragmentHome() {
        supportFragmentManager.apply {
            when (fragments.contains(homeFragment)) {
                true -> {
                    beginTransaction().show(homeFragment).commit()
                    beginTransaction().hide(collectionFragment).commit()
                    beginTransaction().hide(praiseLevelFragment).commit()
                }
                false -> beginTransaction().add(viewBinding.frameLayoutFragmentContainer.id, homeFragment).commit()
            }
        }
    }

    private fun showFragmentCollection() {
        supportFragmentManager.apply {
            when (fragments.contains(collectionFragment)) {
                true -> {
                    beginTransaction().hide(homeFragment).commit()
                    beginTransaction().show(collectionFragment).commit()
                    beginTransaction().hide(praiseLevelFragment).commit()
                }
                false -> beginTransaction().add(viewBinding.frameLayoutFragmentContainer.id, collectionFragment).commit()
            }
        }
    }

    private fun showFragmentPraiseLevel() {
        supportFragmentManager.apply {
            when (fragments.contains(praiseLevelFragment)) {
                true -> {
                    beginTransaction().hide(homeFragment).commit()
                    beginTransaction().hide(collectionFragment).commit()
                    beginTransaction().show(praiseLevelFragment).commit()
                }
                false -> beginTransaction().add(viewBinding.frameLayoutFragmentContainer.id, praiseLevelFragment).commit()
            }
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(viewBinding.frameLayoutFragmentContainer.id, fragment).commit()
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
        showToast("'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.")
        backPressedTime = System.currentTimeMillis()
    }
}