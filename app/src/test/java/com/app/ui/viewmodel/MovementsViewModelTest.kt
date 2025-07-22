package com.app.ui.viewmodel

import app.cash.turbine.test
import com.app.ui.state.MovementsUiState
import com.domain.models.AccountSummary
import com.domain.models.Result
import com.domain.usecases.GetAccountSummaryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class MovementsViewModelTest {
    private lateinit var viewModel: MovementsViewModel
    private lateinit var useCase: GetAccountSummaryUseCase
    private val dispatcher = StandardTestDispatcher()

    private fun createViewModel() {
        viewModel = MovementsViewModel(useCase)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        useCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Initial state is Loading`() =
        runTest {
            coEvery { useCase() } coAnswers {
                delay(1000)
                Result.Loading
            }

            createViewModel()

            assertIs<MovementsUiState.Loading>(viewModel.state.value)
        }

    @Test
    fun `State transitions to Success on successful data load`() =
        runTest {
            val expected = AccountSummary(1000.0, 1500.0, 500.0, emptyList())
            coEvery { useCase() } returns Result.Success(expected)

            createViewModel()
            dispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assertIs<MovementsUiState.Success>(state)
            assertEquals(expected, (state as MovementsUiState.Success).data)
        }

    @Test
    fun `State transitions to Error on data load failure`() =
        runTest {
            val errorMsg = "Something went wrong"
            coEvery { useCase() } returns Result.Error(errorMsg)

            createViewModel()
            dispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assertIs<MovementsUiState.Error>(state)
            assertEquals(errorMsg, (state as MovementsUiState.Error).message)
        }

    @Test
    fun `State transitions to Loading when use case returns Loading`() =
        runTest {
            coEvery { useCase() } returns Result.Loading

            createViewModel()
            dispatcher.scheduler.advanceUntilIdle()

            assertIs<MovementsUiState.Loading>(viewModel.state.value)
        }

    @Test
    fun `State updates correctly on multiple loadData calls`() =
        runTest {
            val success = AccountSummary(1000.0, 1500.0, 500.0, emptyList())
            var callCount = 0

            coEvery { useCase() } answers {
                if (callCount++ == 0) {
                    Result.Success(success)
                } else {
                    Result.Error("Error en segunda llamada")
                }
            }

            createViewModel()

            viewModel.state.test {
                viewModel.loadData()
                assertEquals(MovementsUiState.Loading, awaitItem())
                assertEquals(MovementsUiState.Success(success), awaitItem())

                viewModel.loadData()
                assertEquals(MovementsUiState.Loading, awaitItem())
                assertEquals(MovementsUiState.Error("Error en segunda llamada"), awaitItem())
            }
        }

    @Test
    fun `Coroutine cancellation during data load`() =
        runTest {
            val testScope = TestScope(StandardTestDispatcher(testScheduler))

            coEvery { useCase() } coAnswers {
                delay(10000)
                Result.Success(AccountSummary(0.0, 0.0, 0.0, emptyList()))
            }

            val viewModel = MovementsViewModel(useCase)

            viewModel.state.test {
                assertEquals(MovementsUiState.Loading, awaitItem())
                testScope.cancel()
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Use case throws an unexpected exception`() =
        runTest {
            val message = "Error inesperado al cargar los datos: Explosión inesperada"
            coEvery { useCase() } throws RuntimeException(message)

            createViewModel()

            viewModel.state.test {
                assertEquals(MovementsUiState.Loading, awaitItem())
                val result = awaitItem()

                assertTrue(result is MovementsUiState.Error)
                assertEquals(message, (result as MovementsUiState.Error).message)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
