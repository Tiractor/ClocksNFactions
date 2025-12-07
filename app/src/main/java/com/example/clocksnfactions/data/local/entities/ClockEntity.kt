package com.example.clocksnfactions.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clocks")
data class ClockEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val factionId: Long,   // связь с фракцией
    val name: String,      // название
    val segments: Int,     // количество секций (4,6,8,...)
    val filled: Int,        // количество заполненных секций

    val note: String? = null
)
