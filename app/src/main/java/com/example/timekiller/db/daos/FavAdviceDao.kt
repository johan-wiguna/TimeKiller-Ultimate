package com.example.timekiller.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.timekiller.db.entities.AdviceEntity

@Dao
interface FavAdviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavAdvice(adviceEntity: AdviceEntity): Long

    @Delete
    suspend fun deleteFavAdvice(adviceEntity: AdviceEntity): Int

    @Delete
    suspend fun deleteFavAdvice(adviceEntities: List<AdviceEntity>): Int

    @Query("SELECT * FROM fav_advice ORDER BY timestamp DESC")
    fun getAllFavAdvice(): LiveData<List<AdviceEntity>>

    @Query("SELECT * FROM fav_advice WHERE id = :id")
    suspend fun getFavAdviceById(id: Int): List<AdviceEntity>

    @Query("DELETE FROM fav_advice")
    suspend fun deleteAll() : Int
}