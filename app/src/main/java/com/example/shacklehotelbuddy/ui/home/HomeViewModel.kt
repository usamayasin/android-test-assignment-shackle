package com.example.shacklehotelbuddy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shacklehotelbuddy.base.BaseViewModel
import com.example.shacklehotelbuddy.data.local.entity.SearchQueryEntity
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.usecase.GetRecentSearchQueryUseCase
import com.example.shacklehotelbuddy.data.usecase.SaveSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val getRecentSearchQueryUseCase: GetRecentSearchQueryUseCase,
) : BaseViewModel() {


    private val _searchQuery = MutableLiveData<Boolean>()
    val searchQueryLiveData: LiveData<Boolean> = _searchQuery

    private val _recentSearchQuery = MutableLiveData<SearchQueryEntity>()
    val recentSearchQueryLiveData: LiveData<SearchQueryEntity> = _recentSearchQuery


    fun saveSearchAndProceed(
        propertiesRequestBody: PropertiesRequestBody,
    ) {
        showLoader()
        viewModelScope.launch(Dispatchers.IO) {
            saveSearchQueryUseCase.invoke(propertiesRequestBody = propertiesRequestBody).collect {
                _searchQuery.postValue(it)
            }
        }
    }

    fun getRecentSearchQuery() {
        viewModelScope.launch(Dispatchers.IO) {
            getRecentSearchQueryUseCase.invoke().collect {
                it.collect { result ->
                    _recentSearchQuery.postValue(result)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}

