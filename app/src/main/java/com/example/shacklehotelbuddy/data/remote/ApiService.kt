package com.example.shacklehotelbuddy.data.remote

import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.remote.model.PropertiesResponse
import com.example.shacklehotelbuddy.utils.Constants.PROPERTIES_END_POINT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(PROPERTIES_END_POINT)
    suspend fun getProperties(
        @Body propertiesRequestBody: PropertiesRequestBody
    ): Response<PropertiesResponse>

}
