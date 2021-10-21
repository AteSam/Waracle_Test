package com.example.waracleandroidtest.domain

import com.example.waracleandroidtest.common.Results
import com.example.waracleandroidtest.domain.entity.Cake

interface CakeRepository {
    suspend fun getCakes(): Results<List<Cake>>
}