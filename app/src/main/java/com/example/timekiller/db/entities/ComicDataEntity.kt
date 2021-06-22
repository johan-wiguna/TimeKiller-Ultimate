package com.example.timekiller.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_comic")
data class ComicDataEntity(
    @PrimaryKey(autoGenerate = false)
    var num: Int,
    var link: String,
    var day: Int,
    var month: Int,
    var year: Int,
    var news: String,
    var safe_title: String,
    var transcript: String,
    var alt: String,
    var img: String,
    var title: String,
)