package com.example.praisewhale.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.praisewhale.MainActivity
import com.example.praisewhale.R
import com.example.praisewhale.util.MyApplication

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        createNotificationChannel(context)

        if (MyApplication.mySharedPreferences.getBooleanValue("alarm_onoff", true)) {
            val alarmIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val alarmPendingIntent: PendingIntent =
                PendingIntent.getActivity(
                    context,
                    0,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val convertView = RemoteViews(context.packageName, R.layout.layout_notification)
            convertView.setTextViewText(R.id.tv_alarm_time, timeString())
            val convertHeadUpView =
                RemoteViews(context.packageName, R.layout.layout_head_up_notification)

            val builder =
                NotificationCompat.Builder(context, context.getString(R.string.app_name))
                    .setSmallIcon(R.drawable.push_ic_icon)
                    .setCustomContentView(convertHeadUpView)
                    .setCustomBigContentView(convertView)
                    .setContentIntent(alarmPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // automatically removes the notification when the user taps it.
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(0, builder.build())

            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.app_name)
            val descriptionText = context.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(context.getString(R.string.app_name), name, importance).apply {
                    description = descriptionText
                }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun timeString(): String {
        var hour = MyApplication.mySharedPreferences.getValue("alarm_hour", "14").toInt()
        val minute = MyApplication.mySharedPreferences.getValue("alarm_minute", "40").toInt()

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