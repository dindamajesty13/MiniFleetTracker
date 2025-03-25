package com.majesty.minifleettracker.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.majesty.minifleettracker.R

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val alertMessage = inputData.getString("alert_message") ?: "🚗 Vehicle Alert!"
        showNotification(alertMessage)
        return Result.success()
    }

    private fun showNotification(message: String) {
        val context = applicationContext
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val vibration = longArrayOf(0, 500, 200, 500)

            val channel = NotificationChannel(
                "alert_channel",
                "Vehicle Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts for vehicle status changes"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = vibration
                setSound(soundUri, null)
            }

            notificationManager.createNotificationChannel(channel)
        }

        val vibrationPattern = longArrayOf(0, 500, 200, 500)

        val notification = NotificationCompat.Builder(context, "alert_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🚨 Vehicle Alert")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(vibrationPattern)
            .setLights(Color.RED, 1000, 1000)
            .build()

        notificationManager.notify(1, notification)
    }

}
