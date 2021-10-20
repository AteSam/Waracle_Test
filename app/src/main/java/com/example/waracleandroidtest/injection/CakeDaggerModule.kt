package com.example.waracleandroidtest.injection

import com.example.waracleandroidtest.data.CakeRepositoryImpl
import com.example.waracleandroidtest.data.CakeService
import com.example.waracleandroidtest.domain.CakeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CakeDaggerModule {

    @Provides
    fun provideOkHttpBuilder(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE }
            ).build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient):
            Retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl("https://gist.githubusercontent.com/")
        .build()

    @Provides
    @Singleton
    fun provideCakeService(okHttpClient: OkHttpClient, retrofit: Retrofit):CakeService =
        CakeService.buildService(
            endpoint = "https://gist.githubusercontent.com/",
            okHttpClient = okHttpClient,
            retrofit = retrofit
        )

    @Provides
    @Singleton
    fun provideCakeRepository(cakeService: CakeService): CakeRepository =
        CakeRepositoryImpl(cakeService)
}