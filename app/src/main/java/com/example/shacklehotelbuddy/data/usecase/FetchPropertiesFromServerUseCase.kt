package com.example.shacklehotelbuddy.data.usecase

import com.example.shacklehotelbuddy.data.mapper.Mapper.mapToUiModelOfProperties
import com.example.shacklehotelbuddy.data.remote.DataState
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.remote.model.SingleProperty
import com.example.shacklehotelbuddy.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class FetchPropertiesFromServerUseCase @Inject constructor(
    private val repository: Repository,
) {

    suspend fun invoke(
        propertiesRequestBody: PropertiesRequestBody,
    ): Flow<DataState<List<SingleProperty>>> = flow {

        emit(DataState.Loading())
        repository.getProperties(requestBody = propertiesRequestBody).collect { response ->
            when (response) {
                is DataState.Success -> {
                    val uiModelProperties =
                        response.data!!.data.propertySearch?.properties?.mapToUiModelOfProperties()
                    emit(DataState.Success(uiModelProperties))
                }
                is DataState.Error -> {
                    emit(DataState.Error(response.error))
                }
                else -> {
                    emit(DataState.Error(response.error))
                }
            }
        }
    }
}


