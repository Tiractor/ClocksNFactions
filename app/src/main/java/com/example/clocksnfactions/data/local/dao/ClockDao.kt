package com.example.clocksnfactions.data.local.dao

import androidx.room.*
import com.example.clocksnfactions.data.local.entities.ClockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClockDao {

    @Query("SELECT * FROM clocks WHERE factionId = :factionId")
    fun observeForFaction(factionId: Long): Flow<List<ClockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clock: ClockEntity): Long

    @Update
    suspend fun update(clock: ClockEntity)

    @Delete
    suspend fun delete(clock: ClockEntity)

}
