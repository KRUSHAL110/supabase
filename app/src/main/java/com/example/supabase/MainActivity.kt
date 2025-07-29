package com.example.supabase

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppInstance = staticCompositionLocalOf<Application> {
    error("No Application instance provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appInstance = application

        // Initialize LinphoneManager with app context
        LinphoneManager.start(appInstance)
        LinphoneManager.registerSipAccount("1002", "password", "192.168.7.120", appInstance)

        setContent {
            CompositionLocalProvider(LocalAppInstance provides appInstance) {
                MaterialTheme {
                    AppNavigation(
                        onStartCall = { number: String ->
                            LinphoneManager.startOutgoingCall(number)
                        }
                    )
                }
            }
        }
    }
}