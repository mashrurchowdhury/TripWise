package com.example.tripwise.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tripwise.notifications.NotificationScheduler
import java.util.concurrent.TimeUnit

fun scheduleNotifications(context: Context) {
    Log.d("NotificationScheduler", "Scheduling initial notification")

    val workRequest = OneTimeWorkRequestBuilder<NotificationScheduler>()
//        .setInitialDelay(24, TimeUnit.HOURS)
        .setInitialDelay(0, TimeUnit.SECONDS) // Start immediately for testing
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
    Log.d("NotificationScheduler", "Initial notification scheduled.")
}


fun isNotificationPermissionGranted(context: Context): Boolean {
    Log.d("NotificationScheduler", "Checking permissions")
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
}


