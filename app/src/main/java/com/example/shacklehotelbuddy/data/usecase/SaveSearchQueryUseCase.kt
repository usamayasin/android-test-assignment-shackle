package com.example.shacklehotelbuddy.data.usecase

import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import com.example.shacklehotelbuddy.data.local.repository.AbstractLocalRepo
import com.example.shacklehotelbuddy.data.remote.model.Children
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.utils.convertDateToValidFormat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveSearchQueryUseCase @Inject constructor(
    private val localRepo: AbstractLocalRepo,
) {
    operator fun invoke(
        propertiesRequestBody: PropertiesRequestBody,
    ) = flow {
        localRepo.insertSearchQuery(
            SearchQueryEntity(
                checkInDate = convertDateToValidFormat(propertiesRequestBody.checkInDate!!),
                checkOutDate = convertDateToValidFormat(propertiesRequestBody.checkOutDate!!),
                adults = propertiesRequestBody.rooms!!.first().adults,
                children = checkIfChildrenListIsValid(propertiesRequestBody.rooms!!.first().children)
            )
        )
        emit(true)
    }

    private fun checkIfChildrenListIsValid(list: ArrayList<Children>): Int {
        return if (list.isNotEmpty())
            list.first().age
        else {
            0
        }
    }
}

