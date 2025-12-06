package com.example.clocksnfactions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.clocksnfactions.ui.screens.MainScreen
import com.example.clocksnfactions.ui.viewmodel.FactionViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: FactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем AndroidViewModel через стандартный фабричный провайдер
        viewModel = ViewModelProvider(this).get(FactionViewModel::class.java)

        setContent {
            MainScreen(viewModel = viewModel)
        }
    }
}
