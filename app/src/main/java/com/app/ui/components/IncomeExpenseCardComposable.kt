package com.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.ui.Strings
import com.app.ui.boldTitleMedium
import com.app.ui.smallBodySmall
import com.app.ui.toColor
import com.app.ui.toCurrencyString
import com.domain.models.TransactionType

@Composable
fun IncomeExpenseCardComposable(
    title: String,
    amount: Double,
    isIncome: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (isIncome) TransactionType.INCOME.toColor() else TransactionType.EXPENSE.toColor()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.smallBodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = amount.toCurrencyString(),
            style = MaterialTheme.typography.boldTitleMedium,
            color = color,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IncomeExpenseCardComposablePreview() {
    IncomeExpenseCardComposable(
        title = Strings.INCOME,
        amount = 3500.0,
        isIncome = true,
    )
}
