package com.faris165.mobilecomponentslab.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val CHANNEL_ID = "alarm_channel"

fun ensureChannel(ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val ch = NotificationChannel(CHANNEL_ID, "Alarm", NotificationManager.IMPORTANCE_HIGH)
        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(ch)
    }
}

fun showAlarmNotification(ctx: Context, title: String, text: String) {
    ensureChannel(ctx)
    val nb = NotificationCompat.Builder(ctx, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
        .setContentTitle(title)
        .setContentText(text)
        .setAutoCancel(true)

    try {
        NotificationManagerCompat.from(ctx).notify(1, nb.build())
    } catch (se: SecurityException) {
        // Terjadi jika POST_NOTIFICATIONS tidak diizinkan pada Android 13+
        // Jangan crash proses broadcast
        se.printStackTrace()
    }
}