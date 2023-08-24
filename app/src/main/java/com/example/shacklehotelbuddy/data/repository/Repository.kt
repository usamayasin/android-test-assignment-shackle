package com.example.shacklehotelbuddy.data.repository

import com.example.shacklehotelbuddy.data.remote.DataState
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.remote.model.PropertiesResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository is an interface data layer to handle communication with any data source such as Server or local database.
 * @see [RepositoryImpl] for implementation of this class to utilize APIService.
 */
interface Repository {

    suspend fun getProperties(requestBody: PropertiesRequestBody): Flow<DataState<PropertiesResponse>>
}
