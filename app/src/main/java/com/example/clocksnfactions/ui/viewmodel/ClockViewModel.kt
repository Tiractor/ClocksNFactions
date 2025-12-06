package com.example.clocksnfactions.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.clocksnfactions.App
import com.example.clocksnfactions.data.local.entities.ClockEntity
import com.example.clocksnfactions.data.repository.ClockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClockViewModel(application: Application, private val factionId: Long) :
    AndroidViewModel(application) {

    private val app = application as App
    private val repo = ClockRepository(app.database.clockDao())

    // Храним локальную копию списка — это даёт возможность делать optimistic update
    private val _clocks = MutableStateFlow<List<ClockEntity>>(emptyList())
    val clocks: StateFlow<List<ClockEntity>> = _clocks

    init {
        // Собираем поток из репозитория и логируем эмиссии.
        // collectLatest — если приходят новые эмиссии, предыдущая сборка отменяется.
        viewModelScope.launch {
            repo.observe(factionId)
                .collectLatest { list ->
                    Log.d("VM_FLOW", "repo emitted for faction=$factionId ids=${list.map { it.id }} filled=${list.map { it.filled }}")
                    _clocks.value = list
                }
        }
    }

    fun createClock(name: String, segments: Int) {
        viewModelScope.launch {
            try {
                val id = repo.create(factionId, name, segments)
                Log.d("REPO", "created clock id=$id for faction=$factionId")
            } catch (t: Throwable) {
                Log.e("REPO", "create error", t)
            }
        }
    }

    fun incrementById(clockId: Long) {
        val current = _clocks.value.find { it.id == clockId } ?: run {
            Log.w("CLOCKS", "incrementById: clock not found id=$clockId")
            return
        }
        val newFilled = (current.filled + 1).coerceAtMost(current.segments)
        Log.d("CLOCKS", "incrementById id=$clockId old=${current.filled} new=$newFilled seg=${current.segments}")

        // Optimistic update локально:
        _clocks.update { list -> list.map { if (it.id == clockId) it.copy(filled = newFilled) else it } }

        // Обновление в БД
        viewModelScope.launch {
            try {
                repo.update(current.copy(filled = newFilled))
                Log.d("CLOCKS", "db updated id=$clockId to filled=$newFilled")
            } catch (t: Throwable) {
                Log.e("CLOCKS", "db update failed for id=$clockId", t)
                // rollback
                _clocks.update { list -> list.map { if (it.id == clockId) current else it } }
            }
        }
    }

    fun decrementById(clockId: Long) {
        val current = _clocks.value.find { it.id == clockId } ?: run {
            Log.w("CLOCKS", "decrementById: clock not found id=$clockId")
            return
        }
        val newFilled = (current.filled - 1).coerceAtLeast(0)
        Log.d("CLOCKS", "decrementById id=$clockId old=${current.filled} new=$newFilled")

        _clocks.update { list -> list.map { if (it.id == clockId) it.copy(filled = newFilled) else it } }

        viewModelScope.launch {
            try {
                repo.update(current.copy(filled = newFilled))
                Log.d("CLOCKS", "db updated id=$clockId to filled=$newFilled")
            } catch (t: Throwable) {
                Log.e("CLOCKS", "db update failed for id=$clockId", t)
                _clocks.update { list -> list.map { if (it.id == clockId) current else it } }
            }
        }
    }

    fun delete(clock: ClockEntity) = viewModelScope.launch {
        try {
            repo.delete(clock)
            Log.d("REPO", "deleted id=${clock.id}")
        } catch (t: Throwable) {
            Log.e("REPO", "delete failed id=${clock.id}", t)
        }
    }
}
