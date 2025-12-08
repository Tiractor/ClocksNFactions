package com.example.clocksnfactions.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clocksnfactions.data.local.dao.FactionDao
import com.example.clocksnfactions.data.local.dao.ClockDao
import com.example.clocksnfactions.data.local.entities.FactionEntity
import com.example.clocksnfactions.data.local.entities.ClockEntity

@Database(
    entities = [
        FactionEntity::class,
        ClockEntity::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun factionDao(): FactionDao
    abstract fun clockDao(): ClockDao
}
