package com.example.tflstationclosurenotifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.NotificationCompat

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "MyChannelId"


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        // Handle FCM messages here.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Show notification or handle the payload.
            showNotification(it.title, it.body)
        }


    }


    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            // Use flags to ensure the activity opens correctly
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Create the notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel description"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build and display the notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: Replace with your app's notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Automatically dismiss the notification when tapped

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Send token to your server for later push notifications
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // You can implement this method to send the token to your server.
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }
}
