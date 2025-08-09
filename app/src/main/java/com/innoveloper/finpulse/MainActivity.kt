package com.innoveloper.finpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.innoveloper.finpulse.ui.AppNavigator
import com.innoveloper.finpulse.ui.theme.FinPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinPulseTheme {
                AppNavigator()
            }
        }
    }
}