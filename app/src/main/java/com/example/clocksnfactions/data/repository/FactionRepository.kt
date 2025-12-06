package com.example.clocksnfactions.data.repository

import com.example.clocksnfactions.data.local.dao.FactionDao
import com.example.clocksnfactions.data.local.entities.FactionEntity
import kotlinx.coroutines.flow.Flow

class FactionRepository(private val dao: FactionDao) {

    fun observeAll(): Flow<List<FactionEntity>> = dao.getAll()

    suspend fun insert(name: String): Long {
        val entity = FactionEntity(name = name)
        return dao.insert(entity)
    }
    suspend fun update(f: FactionEntity) {
        dao.update(f)
    }
    suspend fun delete(f: FactionEntity) = dao.delete(f)
}
