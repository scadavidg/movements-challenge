package com.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.ui.boldBodyLarge
import com.app.ui.height4
import com.app.ui.normalBodyLarge
import com.app.ui.padding16
import com.app.ui.smallBodySmall
import com.app.ui.toColor
import com.app.ui.toCurrencyString
import com.app.ui.toFriendlyDate
import com.app.ui.verticalPadding4
import com.domain.models.Transaction
import com.domain.models.TransactionType

@Composable
fun TransactionItemComposable(
    transaction: Transaction,
    modifier: Modifier = Modifier,
) {
    val amountColor = transaction.type.toColor()

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalPadding4(),
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
                    .padding16(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.normalBodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height4())
                Text(
                    text = transaction.date.toFriendlyDate(),
                    style = MaterialTheme.typography.smallBodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = transaction.amount.toCurrencyString(),
                color = amountColor,
                style = MaterialTheme.typography.boldBodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemComposablePreviewExpense() {
    val sampleTransaction =
        Transaction(
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
    val sampleTransaction =
        Transaction(
            id = 1,
            name = "Compra Supermercado",
            amount = 120.75,
            date = "2024-07-16",
            type = TransactionType.INCOME,
        )
    TransactionItemComposable(transaction = sampleTransaction)
}
