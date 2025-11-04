package com.faris165.mobilecomponentslab.alarm


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showAlarmNotification(context, "Alarm Aktif", intent.getStringExtra("msg") ?: "Waktunya!")
    }
}