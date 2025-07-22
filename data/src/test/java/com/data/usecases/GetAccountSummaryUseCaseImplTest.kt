package com.data.usecases

import com.domain.models.AccountDetail
import com.domain.models.Result
import com.domain.models.Transaction
import com.domain.models.TransactionType
import com.domain.repositories.AccountRepository
import com.domain.usecases.CalculateTotalExpensesUseCase
import com.domain.usecases.CalculateTotalIncomeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAccountSummaryUseCaseImplTest {
    private val repository: AccountRepository = mock()
    private val calculateIncome: CalculateTotalIncomeUseCase = mock()
    private val calculateExpenses: CalculateTotalExpensesUseCase = mock()

    private lateinit var useCase: GetAccountSummaryUseCaseImpl

    private val sampleTransactions =
        listOf(
            Transaction(1, "Salary", 1000.0, "2024-01-01", TransactionType.INCOME),
            Transaction(2, "Rent", 500.0, "2024-01-02", TransactionType.EXPENSE),
        )

    @Before
    fun setup() {
        useCase = GetAccountSummaryUseCaseImpl(repository, calculateIncome, calculateExpenses)
    }

    @Test
    fun `Successful account summary retrieval`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(500.0, sampleTransactions)))
            whenever(calculateIncome(sampleTransactions)).thenReturn(1000.0)
            whenever(calculateExpenses(sampleTransactions)).thenReturn(500.0)

            val result = useCase()

            assertTrue(result is Result.Success)
            with((result as Result.Success).data) {
                assertEquals(500.0, balance)
                assertEquals(1000.0, totalIncome)
                assertEquals(500.0, totalExpenses)
                assertEquals(sampleTransactions, transactions)
            }
        }

    @Test
    fun `Repository returns error`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Error("Network Error"))

            val result = useCase()

            assertTrue(result is Result.Error)
            assertEquals("Network Error", (result as Result.Error).message)
        }

    @Test
    fun `Repository returns loading state`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Loading)

            val result = useCase()

            assertTrue(result is Result.Loading)
        }

    @Test
    fun `Empty transaction list`() =
        runTest {
            val emptyList = emptyList<Transaction>()
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(0.0, emptyList)))
            whenever(calculateIncome(emptyList)).thenReturn(0.0)
            whenever(calculateExpenses(emptyList)).thenReturn(0.0)

            val result = useCase()

            assertTrue(result is Result.Success)
            val summary = (result as Result.Success).data
            assertEquals(0.0, summary.totalIncome)
            assertEquals(0.0, summary.totalExpenses)
            assertEquals(0.0, summary.balance)
        }

    @Test
    fun `Transactions with only income`() =
        runTest {
            val txs = listOf(Transaction(1, "Ingreso", 100.0, "2024-01-01", TransactionType.INCOME))
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(100.0, txs)))
            whenever(calculateIncome(txs)).thenReturn(100.0)
            whenever(calculateExpenses(txs)).thenReturn(0.0)

            val result = useCase()
            assertTrue(result is Result.Success)
            assertEquals(100.0, (result as Result.Success).data.totalIncome)
            assertEquals(0.0, result.data.totalExpenses)
        }

    @Test
    fun `Transactions with only expenses`() =
        runTest {
            val txs = listOf(Transaction(1, "Gasto", 50.0, "2024-01-01", TransactionType.EXPENSE))
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(-50.0, txs)))
            whenever(calculateIncome(txs)).thenReturn(0.0)
            whenever(calculateExpenses(txs)).thenReturn(50.0)

            val result = useCase()
            assertTrue(result is Result.Success)
            assertEquals(0.0, (result as Result.Success).data.totalIncome)
            assertEquals(50.0, result.data.totalExpenses)
        }

    @Test
    fun `Transactions with mixed income and expenses`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(500.0, sampleTransactions)))
            whenever(calculateIncome(sampleTransactions)).thenReturn(1000.0)
            whenever(calculateExpenses(sampleTransactions)).thenReturn(500.0)

            val result = useCase()
            assertTrue(result is Result.Success)
            val summary = (result as Result.Success).data
            assertEquals(1000.0, summary.totalIncome)
            assertEquals(500.0, summary.totalExpenses)
        }

    @Test
    fun `Zero balance`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(0.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenReturn(1000.0)
            whenever(calculateExpenses(any())).thenReturn(1000.0)

            val result = useCase()
            assertTrue(result is Result.Success)
            assertEquals(0.0, (result as Result.Success).data.balance)
        }

    @Test
    fun `Negative balance`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(-200.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenReturn(300.0)
            whenever(calculateExpenses(any())).thenReturn(500.0)

            val result = useCase()
            assertTrue(result is Result.Success)
            assertEquals(-200.0, (result as Result.Success).data.balance)
        }

    @Test
    fun `Large balance and transaction amounts`() =
        runTest {
            val largeTransactions =
                listOf(
                    Transaction(1, "Ingreso grande", 1_000_000_000.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Gasto grande", 900_000_000.0, "2024-01-02", TransactionType.EXPENSE),
                )
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(100_000_000.0, largeTransactions)))
            whenever(calculateIncome(largeTransactions)).thenReturn(1_000_000_000.0)
            whenever(calculateExpenses(largeTransactions)).thenReturn(900_000_000.0)

            val result = useCase()
            assertTrue(result is Result.Success)
            assertEquals(100_000_000.0, (result as Result.Success).data.balance)
        }

    @Test
    fun `CalculateTotalIncomeUseCase throws exception`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(0.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenThrow(RuntimeException("Income failure"))

            val result = useCase()
            assertTrue(result is Result.Error)
            assertEquals("Income failure", (result as Result.Error).message)
        }

    @Test
    fun `CalculateTotalIncomeUseCase throws exception with null message`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(0.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenThrow(RuntimeException(null as String?))

            val result = useCase()
            assertTrue(result is Result.Error)
            assertEquals("Unexpected error during calculation", (result as Result.Error).message)
        }

    @Test
    fun `CalculateTotalExpensesUseCase throws exception`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(0.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenReturn(1000.0)
            whenever(calculateExpenses(any())).thenThrow(RuntimeException("Expenses failure"))

            val result = useCase()
            assertTrue(result is Result.Error)
            assertEquals("Expenses failure", (result as Result.Error).message)
        }

    @Test
    fun `calculateTotalExpensesUseCase throws exception with null message`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(0.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenReturn(1000.0)
            whenever(calculateExpenses(any())).thenThrow(RuntimeException(null as String?))

            val result = useCase()
            assertTrue(result is Result.Error)
            assertEquals("Unexpected error during calculation", (result as Result.Error).message)
        }

    @Test
    fun `Concurrent invocations`() =
        runTest {
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(500.0, sampleTransactions)))
            whenever(calculateIncome(any())).thenReturn(1000.0)
            whenever(calculateExpenses(any())).thenReturn(500.0)

            val results = List(10) { useCase() }
            assertTrue(results.all { it is Result.Success })
        }

    @Test
    fun `Transaction data with unusual characters or formats`() =
        runTest {
            val specialTransactions =
                listOf(
                    Transaction(1, "💸 Gasto raro & complejo\n", 99.99, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "", 100.0, "2024-01-02", TransactionType.INCOME),
                )
            whenever(repository.getAccountDetail()).thenReturn(Result.Success(AccountDetail(1.0, specialTransactions)))
            whenever(calculateIncome(any())).thenReturn(100.0)
            whenever(calculateExpenses(any())).thenReturn(99.99)

            val result = useCase()
            assertTrue(result is Result.Success)
            val summary = (result as Result.Success).data
            assertEquals(100.0, summary.totalIncome)
            assertEquals(99.99, summary.totalExpenses)
        }
}
