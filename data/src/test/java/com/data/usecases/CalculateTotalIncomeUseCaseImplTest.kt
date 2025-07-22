package com.data.usecases

import com.domain.models.Transaction
import com.domain.models.TransactionType
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalculateTotalIncomeUseCaseImplTest {
    private val useCase = CalculateTotalIncomeUseCaseImpl()

    @Test
    fun `invoke with empty transaction list`() =
        runTest {
            val result = useCase(emptyList())
            assertEquals(0.0, result)
        }

    @Test
    fun `invoke with no income transactions`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Gasto 1", 100.0, "2024-01-01", TransactionType.EXPENSE),
                    Transaction(2, "Gasto 2", 50.0, "2024-01-02", TransactionType.EXPENSE),
                )
            val result = useCase(transactions)
            assertEquals(0.0, result)
        }

    @Test
    fun `invoke with only income transactions`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso 1", 200.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso 2", 300.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertEquals(500.0, result)
        }

    @Test
    fun `invoke with mixed transaction types`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso", 100.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Gasto", 40.0, "2024-01-02", TransactionType.EXPENSE),
                    Transaction(3, "Otro", 15.0, "2024-01-03", TransactionType.UNKNOWN),
                )
            val result = useCase(transactions)
            assertEquals(100.0, result)
        }

    @Test
    fun `invoke with large number of income transactions`() =
        runTest {
            val transactions =
                List(1_000_000) {
                    Transaction(it, "Ingreso $it", 1.0, "2024-01-01", TransactionType.INCOME)
                }
            val result = useCase(transactions)
            assertEquals(1_000_000.0, result)
        }

    @Test
    fun `invoke with large income amounts`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso 1", 1_000_000_000.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso 2", 2_000_000_000.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertEquals(3_000_000_000.0, result)
        }

    @Test
    fun `invoke with zero amount incomes`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso 0", 0.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso válido", 100.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertEquals(100.0, result)
        }

    @Test
    fun `invoke with negative amount incomes if applicable`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Corrección", -30.0, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso", 80.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertEquals(50.0, result)
        }

    @Test
    fun `invoke with transaction amount being NaN`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso NaN", Double.NaN, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso válido", 70.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertTrue(result.isNaN())
        }

    @Test
    fun `invoke with transaction amount being Infinity`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Ingreso infinito", Double.POSITIVE_INFINITY, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Ingreso normal", 100.0, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertTrue(result.isInfinite())
        }

    @Test
    fun `invoke with decimal income values`() =
        runTest {
            val transactions =
                listOf(
                    Transaction(1, "Freelance", 123.45, "2024-01-01", TransactionType.INCOME),
                    Transaction(2, "Bonus", 76.55, "2024-01-02", TransactionType.INCOME),
                )
            val result = useCase(transactions)
            assertEquals(200.0, result, 0.0001)
        }

    @Test
    fun `invoke includes only INCOME types from all enum values`() =
        runTest {
            val transactions =
                TransactionType.values().mapIndexed { index, type ->
                    Transaction(index, "T$type", 10.0, "2024-01-01", type)
                }
            val result = useCase(transactions)
            assertEquals(10.0, result) // solo uno debe ser INCOME
        }

    @Test
    fun `invoke basic coroutine usage`() =
        runTest {
            val result =
                useCase(
                    listOf(Transaction(1, "Pago", 60.0, "2024-01-01", TransactionType.INCOME)),
                )
            assertEquals(60.0, result)
        }
}
