package com.example.tflstationclosurenotifier

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages here.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Show notification or handle the payload.
        }
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
