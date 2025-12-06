package com.example.clocksnfactions.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.clocksnfactions.App
import com.example.clocksnfactions.data.repository.FactionRepository
import com.example.clocksnfactions.data.local.entities.FactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FactionViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as App
    private val repo = FactionRepository(app.database.factionDao())

    // поток со списком фракций (state holder)
    val factions: StateFlow<List<FactionEntity>> =
        repo.observeAll()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addFaction(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repo.insert(name.trim())
        }
    }

    fun deleteFaction(f: FactionEntity) {
        viewModelScope.launch {
            repo.delete(f)
        }
    }
}
