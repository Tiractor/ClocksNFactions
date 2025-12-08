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
            Log.d("Clock","ClockRepository.create -> inserted id=$id for faction=$fId")
            return id
        } catch (e: Exception) {
            Log.d("Clock", "ClockRepository.create failed for faction=$fId name=$name seg=$seg")
            throw e
        }
    }


    suspend fun update(clock: ClockEntity) = dao.update(clock)

    suspend fun delete(clock: ClockEntity) = dao.delete(clock)
}
