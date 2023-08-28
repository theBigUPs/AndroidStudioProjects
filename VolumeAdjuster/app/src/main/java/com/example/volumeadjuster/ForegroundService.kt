package com.example.volumeadjuster


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class ForegroundService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "MyForegroundServiceChannel"
        const val VOLUME_UP_ACTION = "VOLUME_UP_ACTION"
        const val VOLUME_DOWN_ACTION = "VOLUME_DOWN_ACTION"
        const val VOLUME_MAX_ACTION = "VOLUME_MAX_ACTION"
        const val VOLUME_MUTE_ACTION = "VOLUME_MUTE_ACTION"
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
                VOLUME_MAX_ACTION->{

                    val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
                    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
                }
                VOLUME_UP_ACTION -> {

                    val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
                }
                VOLUME_DOWN_ACTION -> {

                    val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
                }

                VOLUME_MUTE_ACTION->{

                    val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                }


                STOP_ACTION -> {

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
                NotificationManager.IMPORTANCE_LOW
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


        val volumeUpIntent = Intent(this, ForegroundService::class.java)
            .setAction(VOLUME_UP_ACTION)
        val volumeUpPendingIntent = PendingIntent.getService(this, 0, volumeUpIntent,
            PendingIntent.FLAG_IMMUTABLE)


        val volumeDownIntent = Intent(this, ForegroundService::class.java)
            .setAction(VOLUME_DOWN_ACTION)
        val volumeDownPendingIntent = PendingIntent.getService(this, 0, volumeDownIntent,
            PendingIntent.FLAG_IMMUTABLE)



        val volumeMaxIntent = Intent(this, ForegroundService::class.java)
            .setAction(VOLUME_MAX_ACTION)
        val volumeMaxPendingIntent = PendingIntent.getService(this, 0, volumeMaxIntent,
            PendingIntent.FLAG_IMMUTABLE)



        val volumeMuteIntent = Intent(this, ForegroundService::class.java)
            .setAction(VOLUME_MUTE_ACTION)
        val volumeMutePendingIntent = PendingIntent.getService(this, 0, volumeMuteIntent,
            PendingIntent.FLAG_IMMUTABLE)


        customLayout.setOnClickPendingIntent(R.id.cancel, stopPendingIntent)
        customLayout.setOnClickPendingIntent(R.id.vol_up, volumeUpPendingIntent)
        customLayout.setOnClickPendingIntent(R.id.vol_down, volumeDownPendingIntent)
        customLayout.setOnClickPendingIntent(R.id.vol_max, volumeMaxPendingIntent)
        customLayout.setOnClickPendingIntent(R.id.vol_mute, volumeMutePendingIntent)

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