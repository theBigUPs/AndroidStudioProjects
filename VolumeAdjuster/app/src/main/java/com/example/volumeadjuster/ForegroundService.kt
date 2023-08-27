package com.example.volumeadjuster


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat


class ForegroundService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "MyForegroundServiceChannel"
        const val VOLUME_UP_ACTION = "VOLUME_UP_ACTION"
        const val VOLUME_DOWN_ACTION = "VOLUME_DOWN_ACTION"
        const val STOP_ACTION = "STOP_ACTION"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                VOLUME_UP_ACTION -> {
                    // Handle pause action
                    // Example: Pause the ongoing task
                }
                STOP_ACTION -> {
                    // Handle stop action
                    // Example: Stop the foreground service and clean up
                    stopForeground(Service.STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {

        val customLayout = RemoteViews(packageName, R.layout.custom_notification_layout)
        val stopIntent = Intent(this, ForegroundService::class.java)
            .setAction(STOP_ACTION)
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE)

        //customLayout.setOnClickPendingIntent(R.id.pause_button, pausePendingIntent)
        //customLayout.setOnClickPendingIntent(R.id.cancel, stopPendingIntent)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setCustomContentView(customLayout)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(null)

        return notificationBuilder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}