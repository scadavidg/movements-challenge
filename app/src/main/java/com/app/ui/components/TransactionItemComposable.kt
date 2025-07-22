package com.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.domain.models.Transaction
import com.domain.models.TransactionType

@Composable
fun TransactionItemComposable(
    transaction: Transaction,
    modifier: Modifier = Modifier,
) {
    val amountColor =
        when (transaction.type) {
            TransactionType.INCOME -> Color(0xFF51BF72) // Verde más brillante
            TransactionType.EXPENSE -> Color(0xFFBF5153) // Rojo más brillante
            else -> MaterialTheme.colorScheme.onSurface
        }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = transaction.name,
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                        ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = "${transaction.amount}",
                color = amountColor,
                style =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemComposablePreviewExpense() {
    val sampleTransaction = Transaction(
        id = 1,
        name = "Compra Supermercado",
        amount = -120.75,
        date = "2024-07-16",
        type = TransactionType.EXPENSE,
    )
    TransactionItemComposable(transaction = sampleTransaction)
}

@Preview(showBackground = true)
@Composable
fun TransactionItemComposablePreviewIncome() {
    val sampleTransaction = Transaction(
        id = 1,
        name = "Compra Supermercado",
        amount = 120.75,
        date = "2024-07-16",
        type = TransactionType.INCOME,
    )
    TransactionItemComposable(transaction = sampleTransaction)
}

