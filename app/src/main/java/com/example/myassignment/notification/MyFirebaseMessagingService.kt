package com.example.myassignment.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myassignment.R
import com.example.myassignment.persistence.Prefs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.java.KoinJavaComponent

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val prefs: Prefs by KoinJavaComponent.inject(Prefs::class.java)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message received: ${remoteMessage.data}")

        // Optional: Show a notification manually
        remoteMessage.notification?.let {
            Log.d("FCM", "Message received: ${it.title + "Title-"+ it.body ?: "Body"}")
            if (prefs.getIsNotificationsEnabled()) {
                showNotification(it.title ?: "Title", it.body ?: "Body")
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    private fun showNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(this, "fcm_channel")
            .setSmallIcon(R.drawable.ic_notification_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("fcm_channel", "FCM", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, builder.build())
    }
}

