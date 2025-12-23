package com.example.clocksnfactions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.example.clocksnfactions.ui.screens.MainScreen
import com.example.clocksnfactions.ui.screens.SplashScreen
import com.example.clocksnfactions.ui.viewmodel.FactionViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: FactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(FactionViewModel::class.java)

        setContent {
            var isLoading by remember { mutableStateOf(true) }

            MaterialTheme {
                if (isLoading) {
                    SplashScreen { isLoading = false }
                } else {
                    MainScreen(viewModel)
                }
            }
        }

    }
}
