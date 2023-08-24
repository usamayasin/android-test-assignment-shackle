package com.example.shacklehotelbuddy.data.local.repository

import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

abstract class AbstractLocalRepo {

    abstract fun insertSearchQuery(searchQueryEntity: SearchQueryEntity)
    abstract fun getRecentSearchQuery(): Flow<SearchQueryEntity>

}