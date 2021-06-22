package com.example.timekiller.model

import com.example.timekiller.db.entities.ComicDataEntity

class Comic(
    val comicDataEntity: ComicDataEntity
){
    var isFavorite = true
    fun toggleFav(){
        isFavorite = !isFavorite
    }

    override fun toString() = "$comicDataEntity;$isFavorite"
}