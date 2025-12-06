package com.example.clocksnfactions.data.repository

import com.example.clocksnfactions.data.local.dao.ClockDao
import com.example.clocksnfactions.data.local.entities.ClockEntity
import kotlinx.coroutines.flow.Flow

class ClockRepository(private val dao: ClockDao) {

    fun observe(factionId: Long): Flow<List<ClockEntity>> =
        dao.observeForFaction(factionId)



    suspend fun create(fId: Long, name: String, seg: Int): Long {
        return dao.insert(
            ClockEntity(
                factionId = fId,
                name = name,
                segments = seg,
                filled = 0
            )
        )
    }

    suspend fun update(clock: ClockEntity) = dao.update(clock)

    suspend fun delete(clock: ClockEntity) = dao.delete(clock)
}
