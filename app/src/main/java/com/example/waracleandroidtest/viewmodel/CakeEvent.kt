package com.example.waracleandroidtest.viewmodel

sealed class CakeEvent {
    data class CakeLoaded(val data: List<com.example.waracleandroidtest.data.model.Cake>) :CakeEvent()
    object CakeLoading :CakeEvent()
    object CakeError :CakeEvent()
}
