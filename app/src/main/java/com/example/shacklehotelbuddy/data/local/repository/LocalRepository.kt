package com.example.shacklehotelbuddy.data.local.repository

import com.example.shacklehotelbuddy.data.local.dao.SearchQueryDao
import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(var searchQueryDao: SearchQueryDao) :
    AbstractLocalRepo() {

    override fun insertSearchQuery(searchQueryEntity: SearchQueryEntity) {
        searchQueryDao.insertSearchQuery(searchQueryEntity)
    }

    override fun getRecentSearchQuery(): Flow<SearchQueryEntity> {
        return searchQueryDao.getRecentSearchQuery()
    }

}