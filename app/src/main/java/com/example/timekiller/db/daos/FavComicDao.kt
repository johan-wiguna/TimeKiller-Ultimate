package com.example.timekiller.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.timekiller.db.entities.AdviceEntity
import com.example.timekiller.db.entities.ComicDataEntity

@Dao
interface FavComicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavComic(comicDataEntity: ComicDataEntity): Long

    @Delete
    suspend fun deleteFavComic(comicDataEntity: ComicDataEntity): Int

    @Delete
    suspend fun deleteFavComic(comicDataEntities: List<ComicDataEntity>): Int

    @Query ("SELECT * FROM fav_comic")
    fun getAllComic(): LiveData<List<ComicDataEntity>>

    @Query("SELECT * FROM fav_comic WHERE num = :id")
    suspend fun getFavComicById(id: Int): List<ComicDataEntity>

    @Query ("DELETE FROM fav_comic")
    suspend fun deleteAll(): Int
}