package com.example.waracleandroidtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waracleandroidtest.common.Results
import com.example.waracleandroidtest.domain.CakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CakeViewModel  @Inject constructor(
    private val cakeRepository: CakeRepository
): ViewModel() {

    private val _cakeEvent = MutableStateFlow<CakeEvent>(CakeEvent.CakeLoading)
    val cakeEvent: StateFlow<CakeEvent>
        get() = _cakeEvent


    fun getCakes() {
        viewModelScope.launch {
            _cakeEvent.value = CakeEvent.CakeLoading
            when(val results = cakeRepository.getCakes()) {
                is Results.Ok -> {
                    _cakeEvent.value = CakeEvent.CakeLoaded(results.data)
                }
                is Results.Error -> {
                    _cakeEvent.value = CakeEvent.CakeError
                }
            }
        }
    }
}