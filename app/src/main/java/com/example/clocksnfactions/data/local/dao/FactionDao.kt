package com.example.clocksnfactions.data.local.dao

import androidx.room.*
import com.example.clocksnfactions.data.local.entities.FactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FactionDao {

    @Query("SELECT * FROM factions ORDER BY name")
    fun getAll(): Flow<List<FactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(faction: FactionEntity) : Long

    @Delete
    suspend fun delete(faction: FactionEntity) : Int
}
