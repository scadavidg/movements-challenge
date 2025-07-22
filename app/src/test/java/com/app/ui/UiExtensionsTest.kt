package com.app.ui

import androidx.compose.ui.graphics.Color
import com.domain.models.TransactionType
import kotlin.test.Test
import kotlin.test.assertEquals

class UiExtensionsTest {
    @Test
    fun `toCurrencyString returns correct format with and without symbol`() {
        val value = 12345.678
        assertEquals("12,345.68", value.toCurrencyString())
        assertEquals("$12,345.68", value.toCurrencyString(withSymbol = true))
    }

    @Test
    fun `toColor returns correct color for INCOME`() {
        assertEquals(Color(0xFF51BF72), TransactionType.INCOME.toColor())
    }

    @Test
    fun `toColor returns correct color for EXPENSE`() {
        assertEquals(Color(0xFFBF5153), TransactionType.EXPENSE.toColor())
    }

    @Test
    fun `toColor returns unspecified for unknown`() {
        val unknown = TransactionType.valueOf("UNKNOWN") // Si lo tienes
        assertEquals(Color.Unspecified, unknown.toColor())
    }

    @Test
    fun `toFriendlyDate returns formatted date`() {
        assertEquals("22 Jul 2024", "2024-07-22".toFriendlyDate())
    }

    @Test
    fun `toFriendlyDate returns original on malformed date`() {
        assertEquals("invalid-date", "invalid-date".toFriendlyDate())
    }
}
