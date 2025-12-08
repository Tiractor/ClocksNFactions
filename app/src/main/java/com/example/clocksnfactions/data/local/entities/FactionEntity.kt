package com.example.clocksnfactions.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "factions")
data class FactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,

    val rank: Int = 0,               // 0..4
    val controlHard: Boolean = false,// true = Жесткий, false = Слабый
    val relationship: Int = 0,

    val note: String? = null
)
