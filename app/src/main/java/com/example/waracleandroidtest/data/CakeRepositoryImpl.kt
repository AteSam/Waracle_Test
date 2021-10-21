package com.example.waracleandroidtest.data

import com.example.waracleandroidtest.common.Results
import com.example.waracleandroidtest.domain.CakeRepository
import com.example.waracleandroidtest.domain.entity.Cake
import retrofit2.HttpException
import javax.inject.Inject

class CakeRepositoryImpl @Inject constructor(
    private val cakeService: CakeService
):CakeRepository {

    override suspend fun getCakes(): Results<List<Cake>> {
        val responseWrapper = cakeService.getCakeResponse()
        when {
            responseWrapper.isSuccessful-> {
                val cakes = responseWrapper.body()?.map {
                    Cake(it.title, it.desc , it.image)
                }
                return Results.Ok(cakes.orEmpty())
            }
            else -> throw  HttpException(responseWrapper)
        }
    }
}