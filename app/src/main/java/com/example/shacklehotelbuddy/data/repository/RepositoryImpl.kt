package com.example.shacklehotelbuddy.data.repository

import androidx.annotation.WorkerThread
import com.example.shacklehotelbuddy.data.remote.*
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.remote.model.PropertiesResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This is an implementation of [Repository] to handle communication with [ApiService] server.
 */

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : Repository {

    @WorkerThread
    override suspend fun getProperties(requestBody: PropertiesRequestBody) = flow {
        apiService.getProperties(propertiesRequestBody = requestBody).apply {
            if (this.isSuccessful) {
                body()?.data?.propertySearch?.properties?.let {
                    emit(DataState.Success(body()))
                } ?: run {
                    emit(DataState.Error<PropertiesResponse>(DataState.CustomMessages.EmptyData))
                }
            } else {
                this.errorBody()?.let {
                    emit(DataState.Error<PropertiesResponse>(DataState.CustomMessages.InternalServerError))
                } ?: run {
                    emit(DataState.Error<PropertiesResponse>(DataState.CustomMessages.BadRequest))
                }
            }
        }

    }.catch {
        this.emit(DataState.Error<PropertiesResponse>(DataState.CustomMessages.SomethingWentWrong(it.message
            ?: DataState.CustomMessages.BadRequest.toString())))
    }

}
