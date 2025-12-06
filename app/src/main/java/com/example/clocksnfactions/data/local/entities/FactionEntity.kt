package com.example.clocksnfactions.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "factions")
data class FactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)
