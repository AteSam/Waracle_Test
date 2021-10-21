package com.example.waracleandroidtest.viewmodel

import com.example.waracleandroidtest.domain.entity.Cake

sealed class CakeItemAction {
    object FetchCakes:CakeItemAction()
    data class CakeItemClicked(val cake: Cake):CakeItemAction()
}