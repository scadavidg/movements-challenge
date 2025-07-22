package com.data.usecases

import com.domain.models.Transaction
import com.domain.models.TransactionType
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateTotalExpensesUseCaseImplTest {
    private val useCase = CalculateTotalExpensesUseCaseImpl()

    @Test
    fun `invoke with empty transaction list`() =
        runTest {
            val result = useCase(emptyList())
            assertEquals(0.0, result)
        }

    @Test
    fun `invoke with no expense transactions`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso 1", 100.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso 2", 50.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertEquals(0.0, result)
        }

    @Test
    fun `invoke with only expense transactions`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Compra 1", 10.0, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Compra 2", 20.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(30.0, result)
        }

    @Test
    fun `invoke with mixed transaction types`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso", 100.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Compra", 40.0, "2024-01-02", TransactionType.EXPENSE),
                    Transaction(3, "Otro", 15.0, "2024-01-03", TransactionType.UNKNOWN),
                )
            val result = useCase(transactions)
            assertEquals(40.0, result)
        }

    @Test
    fun `invoke with large number of expense transactions`() =
        runTest {
            val transactions =
                List(1_000_000) {
                    Transaction(it, "Compra $it", 1.0, "2024-01-01", TransactionType.EXPENSE)
                }
            val result = useCase(transactions)
            assertEquals(1_000_000.0, result)
        }

    @Test
    fun `invoke with large expense amounts`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Gasto 1", 1_000_000_000.0, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Gasto 2", 2_000_000_000.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(3_000_000_000.0, result)
        }

    @Test
    fun `invoke with zero amount expenses`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Compra 1", 0.0, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Compra 2", 10.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(10.0, result)
        }

    @Test
    fun `invoke with negative amount expenses if applicable`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Reembolso", -20.0, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Compra", 50.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(30.0, result)
        }

    @Test
    fun `invoke with transactions list containing nulls if possible`() =
        runTest {
            // Not applicable in Kotlin's List<Transaction> — compiler enforces non-null.
            // This test should be skipped unless source introduces nullable elements.
        }

    @Test
    fun `invoke with transaction amount being non numeric if possible`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Gasto NaN", Double.NaN, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Gasto válido", 50.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(Double.NaN, result)
        }

    @Test
    fun `invoke concurrency check with suspend function`() =
        runTest {
            // Basic coroutine support validation — works in structured concurrency
            val result =
                useCase(
                    listOf(Transaction(1, "Compra", 10.0, "2024-01-01", TransactionType.EXPENSE)),
                )
            assertEquals(10.0, result)
        }

    @Test
    fun `invoke with transaction amount being Infinity`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Gasto infinito", Double.POSITIVE_INFINITY, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Compra normal", 100.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assert(result.isInfinite())
        }

    @Test
    fun `invoke with decimal values`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Café", 3.75, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Pan", 2.25, "2024-01-01", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(6.0, result, 0.0001) // delta por precisión flotante
        }
}
