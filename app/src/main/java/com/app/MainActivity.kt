package com.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.app.ui.screens.MovementsListComposable
import com.app.ui.theme.MovementsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovementsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovementsListComposable(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
