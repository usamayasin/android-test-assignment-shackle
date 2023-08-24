package com.example.shacklehotelbuddy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shacklehotelbuddy.data.local.dao.SearchQueryDao
import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import com.example.shacklehotelbuddy.utils.Constants.DATABASE_NAME

@Database(
    entities = [SearchQueryEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}