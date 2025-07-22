package com.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import com.app.ui.Strings

@Composable
fun IncomeExpenseCardComposable(
    title: String,
    amount: Double,
    isIncome: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (isIncome) Color(0xFF51BF72) else Color(0xFFBF5153)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "%,.2f".format(amount.absoluteValue),
            style =
                MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
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
