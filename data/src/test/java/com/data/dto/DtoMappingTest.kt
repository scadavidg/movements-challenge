package com.data.dto

import com.domain.models.TransactionType
import org.junit.Test
import kotlin.test.assertEquals

class DtoMappingTest {
    @Test
    fun `TransactionDto toDomain maps fields correctly`() {
        val dto =
            TransactionDto(
                id = 10,
                name = "Pago de Luz",
                amount = -150.75,
                date = "2024-07-10",
                type = "egreso",
            )

        val domain = dto.toDomain()

        assertEquals(10, domain.id)
        assertEquals("Pago de Luz", domain.name)
        assertEquals(-150.75, domain.amount)
        assertEquals("2024-07-10", domain.date)
        assertEquals(TransactionType.EXPENSE, domain.type)
    }

    @Test
    fun `TransactionDto with unknown type maps to TransactionType UNKNOWN`() {
        val dto =
            TransactionDto(
                id = 20,
                name = "Tipo extraño",
                amount = 500.0,
                date = "2024-07-11",
                type = "otro_tipo",
            )

        val domain = dto.toDomain()

        assertEquals(TransactionType.UNKNOWN, domain.type)
    }

    @Test
    fun `RecordDto toDomain maps nested transactions correctly`() {
        val recordDto =
            RecordDto(
                balance = 999.99,
                transactions =
                    listOf(
                        TransactionDto(
                            id = 1,
                            name = "Ingreso A",
                            amount = 1000.0,
                            date = "2024-07-01",
                            type = "ingreso",
                        ),
                        TransactionDto(
                            id = 2,
                            name = "Gasto B",
                            amount = -200.0,
                            date = "2024-07-02",
                            type = "egreso",
                        ),
                    ),
            )

        val domain = recordDto.toDomain()

        assertEquals(999.99, domain.balance)
        assertEquals(2, domain.transactions.size)
        assertEquals("Ingreso A", domain.transactions[0].name)
        assertEquals(TransactionType.INCOME, domain.transactions[0].type)
        assertEquals("Gasto B", domain.transactions[1].name)
        assertEquals(TransactionType.EXPENSE, domain.transactions[1].type)
    }
}
