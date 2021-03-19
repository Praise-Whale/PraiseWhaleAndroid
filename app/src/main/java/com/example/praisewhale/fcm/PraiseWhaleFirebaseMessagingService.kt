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
import com.example.praisewhale.util.LAST_PRAISE_STATUS
import com.example.praisewhale.util.MyApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

class PraiseWhaleFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        if (MyApplication.mySharedPreferences.getValue(LAST_PRAISE_STATUS, "") == "") {
            notification(p0)
        }
    }

    private fun notification(p0: RemoteMessage) {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val convertView = RemoteViews(packageName, R.layout.layout_notification)
        convertView.setTextViewText(R.id.tv_alarm_time, timeString())
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

    private fun timeString() = SimpleDateFormat("a h:mm", Locale.KOREA).format(Date())
}