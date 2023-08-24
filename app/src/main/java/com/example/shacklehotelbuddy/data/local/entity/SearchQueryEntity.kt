package com.example.shacklehotelbuddy.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shacklehotelbuddy.utils.Constants

@Entity(tableName = Constants.TABLE_SEARCH_QUERIES)
data class SearchQueryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    val checkInDate: String,
    val checkOutDate: String,
    val adults: Int,
    val children: Int,
    val created_at: Long = System.currentTimeMillis()

)