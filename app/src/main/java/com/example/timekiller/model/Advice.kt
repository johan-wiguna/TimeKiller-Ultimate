package com.example.timekiller.model

import com.example.timekiller.db.entities.AdviceEntity

class Advice(
     val adviceEntity: AdviceEntity
){
    var isFavorite = true
    fun toggleFav(){
        isFavorite = !isFavorite
    }

    override fun toString() = "$adviceEntity;$isFavorite"
}