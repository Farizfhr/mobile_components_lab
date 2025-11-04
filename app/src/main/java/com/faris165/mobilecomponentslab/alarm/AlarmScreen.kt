package com.faris165.mobilecomponentslab.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun AlarmScreen() {
    val ctx = LocalContext.current
    var message by remember { mutableStateOf("Sudah 5 detik berlalu!") }

    // Launcher untuk POST_NOTIFICATIONS (Android 13+)
    val notifPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(ctx, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasPostNotifications(ctx)) {
                notifPermLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Pesan notifikasi") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                requestExactAlarmIfNeeded(ctx) {
                    setExactAfter(ctx, 5_000L, message)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Set Alarm 5 Detik Lagi") }

        Button(
            onClick = {
                requestExactAlarmIfNeeded(ctx) {
                    setExactAfter(ctx, 60_000L, "Satu menit!")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Set Alarm 1 Menit") }
    }
}

private fun setExactAfter(ctx: Context, delayMs: Long, msg: String) {
    ensureChannel(ctx)

    val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(ctx, AlarmReceiver::class.java).putExtra("msg", msg)

    val pi = PendingIntent.getBroadcast(
        ctx,
        /* gunakan requestCode unik kalau perlu beberapa alarm */ 1,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val triggerAt = System.currentTimeMillis() + delayMs

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
            // Fallback agar tidak crash jika exact belum diizinkan
            am.set(AlarmManager.RTC_WAKEUP, triggerAt, pi)
            Toast.makeText(ctx, "Exact alarm belum diizinkan. Menggunakan alarm biasa.", Toast.LENGTH_LONG).show()
            return
        }
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        Toast.makeText(ctx, "Alarm diset!", Toast.LENGTH_SHORT).show()
    } catch (se: SecurityException) {
        Toast.makeText(ctx, "Butuh izin Exact Alarm/Notifikasi. Aktifkan di Pengaturan.", Toast.LENGTH_LONG).show()
    } catch (t: Throwable) {
        Toast.makeText(ctx, "Gagal set alarm: ${t.message}", Toast.LENGTH_LONG).show()
    }
}

/* ===== Helpers ===== */

private fun requestExactAlarmIfNeeded(ctx: Context, onOk: () -> Unit) {
    val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!am.canScheduleExactAlarms()) {
            // Buka layar pengaturan untuk mengaktifkan Exact Alarms
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            ctx.startActivity(intent)
            Toast.makeText(ctx, "Aktifkan Exact Alarms, lalu coba lagi.", Toast.LENGTH_LONG).show()
            return
        }
    }
    onOk()
}

private fun hasPostNotifications(ctx: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            ctx, android.Manifest.permission.POST_NOTIFICATIONS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    } else true
}