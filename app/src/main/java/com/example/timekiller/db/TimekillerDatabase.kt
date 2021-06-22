package com.example.timekiller.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.timekiller.db.daos.FavAdviceDao
import com.example.timekiller.db.daos.FavComicDao
import com.example.timekiller.db.entities.AdviceEntity
import com.example.timekiller.db.entities.ComicDataEntity

@Database(entities = [AdviceEntity::class, ComicDataEntity::class], version = 6)
abstract class TimekillerDatabase : RoomDatabase() {
    abstract val favAdviceDao: FavAdviceDao
    abstract val favComicDao: FavComicDao

    companion object {
        @Volatile
        private var INSTANCE: TimekillerDatabase? = null
        fun getInstance(context: Context): TimekillerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimekillerDatabase::class.java,
                        "timekiller_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}