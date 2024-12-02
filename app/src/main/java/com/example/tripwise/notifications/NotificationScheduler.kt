package com.example.tripwise.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NotificationScheduler(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("NotificationScheduler", "Sending notification")
        sendNotification()
        reEnqueueWork()
        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "tripwise_notifications"
        val channelName = "TripWise Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("TripWise Notification")
            .setContentText("This is a reminder to log your daily expenses!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d("NotificationScheduler", "Notification sent.")
    }

    private fun reEnqueueWork() {
        val nextWorkRequest = OneTimeWorkRequestBuilder<NotificationScheduler>()
//            .setInitialDelay(24, TimeUnit.HOURS)
            .setInitialDelay(45, TimeUnit.SECONDS) // Delay of 30 seconds
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)
        Log.d("NotificationScheduler", "Next notification scheduled in 30 seconds.")
    }
}
