package com.data.repositories

import com.data.api.AccountApiService
import com.data.dto.RecordDto
import com.data.dto.TransactionDto
import com.data.dto.TransactionResponseDto
import com.domain.models.Result
import com.domain.models.TransactionType
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.coroutines.cancellation.CancellationException

@RunWith(MockitoJUnitRunner::class)
class AccountRepositoryImplTest {
    @Mock
    private lateinit var apiService: AccountApiService

    private lateinit var repository: AccountRepositoryImpl

    @Before
    fun setUp() {
        repository = AccountRepositoryImpl(apiService)
    }

    @Test
    fun `getAccountDetail successful response`() =
        runTest {
            val response =
                TransactionResponseDto(
                    RecordDto(
                        balance = 1000.0,
                        transactions =
                            listOf(
                                TransactionDto(1, "Salary", 3000.0, "2024-07-15", "ingreso"),
                            ),
                    ),
                )
            whenever(apiService.getAccountDetail()).thenReturn(response)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Success)
            val data = (result as Result.Success).data
            assertEquals(1000.0, data.balance, 0.01)
            assertEquals(1, data.transactions.size)
            assertEquals("Salary", data.transactions[0].name)
        }

    @Test
    fun `getAccountDetail API call exception`() =
        runTest {
            whenever(apiService.getAccountDetail()).thenThrow(RuntimeException("network error"))

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Error)
            assertEquals("network error", (result as Result.Error).message)
        }

    @Test
    fun `getAccountDetail API call exception with null message`() =
        runTest {
            val exception = object : RuntimeException(null as String?) {}
            whenever(apiService.getAccountDetail()).thenThrow(exception)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Error)
            assertEquals("Unknown error", (result as Result.Error).message)
        }

    @Test
    fun `getAccountDetail toDomain mapping successful`() =
        runTest {
            val dto = TransactionDto(1, "Salary", 3000.0, "2024-07-15", "ingreso")
            val record = RecordDto(2000.0, listOf(dto))
            val response = TransactionResponseDto(record)

            whenever(apiService.getAccountDetail()).thenReturn(response)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Success)
            val domain = (result as Result.Success).data
            assertEquals(2000.0, domain.balance, 0.01)
            assertEquals("Salary", domain.transactions[0].name)
            assertEquals(TransactionType.INCOME, domain.transactions[0].type)
        }

    @Test
    fun `getAccountDetail toDomain mapping throws exception`() =
        runTest {
            val faultyRecordDto =
                mock<RecordDto> {
                    on { toDomain() } doThrow RuntimeException("Mapping failed")
                }

            val responseDto =
                mock<TransactionResponseDto> {
                    on { record } doReturn faultyRecordDto
                }

            whenever(apiService.getAccountDetail()).thenReturn(responseDto)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Error)
            assertEquals("Mapping failed", (result as Result.Error).message)
        }

    @Test
    fun `getAccountDetail handles cancellation`() =
        runTest {
            val job =
                launch {
                    val exception = CancellationException("Cancelled")
                    whenever(apiService.getAccountDetail()).thenThrow(exception)

                    val result = repository.getAccountDetail()
                    assertTrue(result is Result.Error)
                    assertEquals("Cancelled", (result as Result.Error).message)
                }
            job.cancelAndJoin()
        }

    @Test
    fun `getAccountDetail handles empty response record`() =
        runTest {
            val response =
                TransactionResponseDto(
                    record = RecordDto(0.0, emptyList()),
                )
            whenever(apiService.getAccountDetail()).thenReturn(response)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Success)
            val data = (result as Result.Success).data
            assertEquals(0.0, data.balance, 0.01)
            assertTrue(data.transactions.isEmpty())
        }

    @Test
    fun `getAccountDetail handles specific HTTP error codes`() =
        runTest {
            val exception =
                retrofit2.HttpException(
                    retrofit2.Response.error<Any>(404, okhttp3.ResponseBody.create(null, "Not found")),
                )
            whenever(apiService.getAccountDetail()).thenThrow(exception)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Error)
            assertTrue((result as Result.Error).message.contains("HTTP"))
        }

    @Test
    fun `getAccountDetail handles network timeout`() =
        runTest {
            class FakeTimeoutException : RuntimeException("timeout")

            whenever(apiService.getAccountDetail()).thenThrow(FakeTimeoutException())

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Error)
            assertEquals("timeout", (result as Result.Error).message)
        }

    @Test
    fun `getAccountDetail with malformed success response`() =
        runTest {
            val brokenDto =
                TransactionResponseDto(
                    record =
                        RecordDto(
                            balance = 1234.56,
                            transactions =
                                listOf(
                                    TransactionDto(1, "", Double.NaN, "invalid-date", "??"),
                                ),
                        ),
                )
            whenever(apiService.getAccountDetail()).thenReturn(brokenDto)

            val result = repository.getAccountDetail()

            assertTrue(result is Result.Success)
        }
}
