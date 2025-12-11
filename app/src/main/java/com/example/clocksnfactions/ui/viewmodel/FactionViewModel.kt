package com.example.clocksnfactions.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.clocksnfactions.App
import com.example.clocksnfactions.data.local.entities.FactionEntity
import com.example.clocksnfactions.data.repository.FactionRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FactionViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as App
    private val repo = FactionRepository(app.database.factionDao())

    val factions: StateFlow<List<FactionEntity>> = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addFaction(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repo.insert(name.trim())
        }
    }

    fun deleteFaction(f: FactionEntity) {
        viewModelScope.launch { repo.delete(f) }
    }

    fun updateNoteForFaction(f: FactionEntity, newNote: String?) {
        val normalized = newNote?.takeIf { it.isNotBlank() }
        viewModelScope.launch {
            repo.update(f.copy(note = normalized))
        }
    }
    fun updateFaction(f: FactionEntity) {
        viewModelScope.launch {
            repo.update(f)
        }
    }

    fun updateRank(f: FactionEntity, delta: Int) {
        val newRank = (f.rank + delta).coerceIn(0, 6)
        if (newRank == f.rank) return
        viewModelScope.launch {
            repo.update(f.copy(rank = newRank))
        }
    }

    fun toggleControl(f: FactionEntity) {
        viewModelScope.launch {
            repo.update(f.copy(controlHard = !f.controlHard))
        }
    }

    fun updateRelationship(f: FactionEntity, delta: Int) {
        val newRel = (f.relationship + delta).coerceIn(-3, 3)
        if (newRel == f.relationship) return
        viewModelScope.launch {
            repo.update(f.copy(relationship = newRel))
        }
    }
}