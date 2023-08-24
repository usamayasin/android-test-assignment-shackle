package com.example.shacklehotelbuddy.data.usecase

import com.example.shacklehotelbuddy.data.local.repository.AbstractLocalRepo
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentSearchQueryUseCase @Inject constructor(
    private val localRepo: AbstractLocalRepo,
) {
    operator fun invoke() = flow {
        emit(localRepo.getRecentSearchQuery())
    }
}

