package com.example.clocksnfactions.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// ClockViewModelFactory.kt
class ClockViewModelFactory(
    private val application: Application,
    private val factionId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClockViewModel(application, factionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


