package com.example.shacklehotelbuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SearchQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSearchQuery(entity: SearchQueryEntity)

    @Query("SELECT * FROM searchQueries ORDER BY created_at DESC LIMIT 1")
    abstract fun getRecentSearchQuery(): Flow<SearchQueryEntity>

}