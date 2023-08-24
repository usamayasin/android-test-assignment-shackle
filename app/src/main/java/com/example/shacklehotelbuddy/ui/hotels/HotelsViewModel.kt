package com.example.shacklehotelbuddy.ui.hotels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shacklehotelbuddy.base.BaseViewModel
import com.example.shacklehotelbuddy.data.remote.DataState
import com.example.shacklehotelbuddy.data.remote.model.PropertiesRequestBody
import com.example.shacklehotelbuddy.data.remote.model.SingleProperty
import com.example.shacklehotelbuddy.data.usecase.FetchPropertiesFromServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HotelsViewModel @Inject constructor(
    private val fetchPropertiesFromServerUseCase: FetchPropertiesFromServerUseCase,
) : BaseViewModel() {

    private val _properties = MutableLiveData<List<SingleProperty>>()
    val propertiesLiveData: LiveData<List<SingleProperty>> = _properties

    fun getProperties(
        propertiesRequestBody: PropertiesRequestBody,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchPropertiesFromServerUseCase.invoke(propertiesRequestBody).collect { dataState ->
                withContext(Dispatchers.Main) {
                    when (dataState) {
                        is DataState.Success -> {
                            hideLoading()
                            dataState.data?.let {
                                _properties.postValue(it)
                            } ?: run {
                                onResponseComplete(DataState.CustomMessages.EmptyData)
                            }
                        }
                        is DataState.Error -> {
                            hideLoading()
                            onResponseComplete(dataState.error)
                        }
                        is DataState.Loading -> {
                            showLoader()
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }

}

