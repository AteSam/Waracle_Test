package com.example.waracleandroidtest.viewmodel

import com.example.waracleandroidtest.domain.entity.Cake

sealed class CakeEvent {
    data class CakeLoaded(val data: List<Cake>) :CakeEvent()
    object CakeLoading :CakeEvent()
    object CakeError :CakeEvent()
}
