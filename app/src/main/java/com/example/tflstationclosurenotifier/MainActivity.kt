package com.example.tflstationclosurenotifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.messaging.FirebaseMessaging
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tflstationclosurenotifier.ui.theme.TflStationClosureNotifierTheme

class MainActivity : ComponentActivity() {

    private val PERMISSION_REQUEST_CODE = 1000


    private fun sendTokenToServer(token: String) {
        // Implement sending the token to your server here (if necessary)
        Log.d(TAG, "Token sent to server: $token")
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can show notifications
                } else {
                    // Permission denied, handle accordingly
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                // Request notification permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE)
            }
        }
        

        // Create notification channel if the Android version is 8.0 or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel",
                "Default Channel",
                //NotificationManager.IMPORTANCE_DEFAULT
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This is the default notification channel"
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        
        
        
        
        
        

        // Retrieve the FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get the token
            val token = task.result
            Log.d(TAG, "FCM Token: $token")

            // Send the token to your server if needed
            sendTokenToServer(token)
        }


        setContent {
            TflStationClosureNotifierTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TflStationClosureNotifierTheme {
        Greeting("Android")
    }
}