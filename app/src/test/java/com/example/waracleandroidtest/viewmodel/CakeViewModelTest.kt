package com.example.waracleandroidtest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.waracleandroidtest.common.Results
import com.example.waracleandroidtest.domain.CakeRepository
import com.example.waracleandroidtest.domain.entity.Cake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CakeViewModelTest {

    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Mock
    lateinit var cakeRepository: CakeRepository

    private lateinit var viewModel: CakeViewModel


    @Before
    fun doBefore() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = CakeViewModel(cakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun laodCakes_success() = runBlockingTest {
        val cakes = listOf<Cake>(
            Cake(
                "Lemon cheesecake",
                "A cheesecake made of lemon",
                "https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg"
            ),
            Cake(
                "victoria sponge",
                "sponge with jam",
                "https://upload.wikimedia.org/wikipedia/commons/0/05/111rfyh.jpg"
            ),
        )

        whenever(cakeRepository.getCakes()).thenReturn(Results.Ok(cakes))
        viewModel.submitAction(CakeItemAction.FetchCakes)
        viewModel.cakeEvent.asLiveData().observeForever() {
            assertEquals((it as CakeEvent.CakeLoaded).data, cakes)
            assertEquals((it as CakeEvent.CakeLoaded).data.count(), 2)
        }
    }

    @Test
    fun laodCakes_fail() = runBlockingTest {
        whenever(cakeRepository.getCakes()).thenReturn(Results.Error(RuntimeException("Api failed")))
        viewModel.submitAction(CakeItemAction.FetchCakes)
        viewModel.cakeEvent.asLiveData().observeForever() {
            assertEquals(it.javaClass, CakeEvent.CakeError.javaClass)

        }
    }
}