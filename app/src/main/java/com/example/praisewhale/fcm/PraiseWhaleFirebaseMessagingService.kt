package com.example.praisewhale.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.praisewhale.R
import com.example.praisewhale.SplashActivity
import com.example.praisewhale.util.MyApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PraiseWhaleFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        notification(p0)
    }

    private fun notification(p0: RemoteMessage) {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val convertView = RemoteViews(packageName, R.layout.layout_notification)
//        convertView.setTextViewText(R.id.tv_alarm_time, timeString())
//        val convertHeadUpView =
//            RemoteViews(packageName, R.layout.layout_head_up_notification)

        val builder =
            NotificationCompat.Builder(this, getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(convertView)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(getString(R.string.app_name), name, importance).apply {
                    description = descriptionText
                }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())

    }

    private fun timeString(): String {
        var hour = MyApplication.mySharedPreferences.getValue("alarm_hour", "9").toInt()
        val minute = MyApplication.mySharedPreferences.getValue("alarm_minute", "00")

        if (hour >= 12) {
            if (hour in 12..21) {
                hour -= 12
                return "오후 0$hour:$minute"
            }
            if (hour >= 22) {
                hour -= 12
                return "오후 $hour:$minute"
            }
        } else {
            if (hour in 0..9) {
                return "오전 0$hour:$minute"
            }
            if (hour >= 10) {
                return "오전 $hour:$minute"
            }
        }
        return ""
    }
}