package com.example.waracleandroidtest.data

import com.example.waracleandroidtest.common.valueOrThrow
import com.example.waracleandroidtest.domain.CakeRepository
import com.example.waracleandroidtest.domain.entity.Cake
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class CakeRepositoryImplTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var cakeRepository: CakeRepository

    @Before
    fun doBefore() {
        val baseUrl = mockWebServer.url("/")
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
        val okHttpClient = OkHttpClient()
        cakeRepository = CakeRepositoryImpl(cakeService = CakeService.buildService(baseUrl.toString(),
            okHttpClient = okHttpClient, retrofit = retrofit))
    }

    @Test
    fun getCakeResponse_success() {
        runBlocking {
            val expected = listOf<Cake>(
                Cake("Lemon cheesecake",
                    "A cheesecake made of lemon",
                    "https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg"),
                Cake("victoria sponge",
                    "sponge with jam",
                    "https://upload.wikimedia.org/wikipedia/commons/0/05/111rfyh.jpg")
            )

            mockWebServer.enqueue(successString(CAKE_SUCCESS_RESPONSE))
            val actual = cakeRepository.getCakes().valueOrThrow()
            assertEquals(expected, actual)
            assertEquals(2, actual.count())
        }
    }

    @Test
    fun getCakeResponse_404_failure() {
        runBlocking {
            val expected = emptyList<Cake>()
            mockWebServer.enqueue(MockResponse().setResponseCode(404))
            val actual = cakeRepository.getCakes().valueOrThrow()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun getCakeResponseEmpty() {
        runBlocking {
            val expected = emptyList<Cake>()
            mockWebServer.enqueue(successString(CAKE_EMPTY_RESPONSE))
            val actual = cakeRepository.getCakes().valueOrThrow()
            assertEquals(expected, actual)
        }
    }

    private fun successString(file:String) = MockResponse().setBody(getResourceAsString(file))

    private fun getResourceAsString(filename: String): String  {
        return try {
            CakeRepositoryImplTest::class.java.getResourceAsStream(filename).bufferedReader().readText()
        } catch (e: Exception) {
            return  ""
        }
    }
    companion object {
        private const val CAKE_SUCCESS_RESPONSE = "/json/cake/cakeSuccessResponse.json"
        private const val CAKE_EMPTY_RESPONSE = "/json/cake/cakeEmptyResponse.json"

    }
}