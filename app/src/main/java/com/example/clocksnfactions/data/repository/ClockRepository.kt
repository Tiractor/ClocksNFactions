package com.example.clocksnfactions.data.repository

import android.util.Log
import com.example.clocksnfactions.data.local.dao.ClockDao
import com.example.clocksnfactions.data.local.entities.ClockEntity
import kotlinx.coroutines.flow.Flow

class ClockRepository(private val dao: ClockDao) {

    fun observe(factionId: Long): Flow<List<ClockEntity>> =
        dao.observeForFaction(factionId)
    suspend fun create(fId: Long, name: String, seg: Int): Long {
        try {
            val entity = ClockEntity(factionId = fId, name = name, segments = seg, filled = 0)
            val id = dao.insert(entity)
            return id
        } catch (e: Exception) {
            throw e
        }
    }
    suspend fun update(clock: ClockEntity) = dao.update(clock)

    suspend fun delete(clock: ClockEntity) = dao.delete(clock)
}
