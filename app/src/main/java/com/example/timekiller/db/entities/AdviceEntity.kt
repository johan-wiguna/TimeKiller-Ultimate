package com.example.timekiller.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_advice")
data class AdviceEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var advice: String,
    var timestamp: Long
    )