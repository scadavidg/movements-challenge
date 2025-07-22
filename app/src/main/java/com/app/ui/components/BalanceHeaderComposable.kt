import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.ui.Strings
import com.app.ui.boldDisplayLarge
import com.app.ui.components.IncomeExpenseCardComposable
import com.app.ui.height24
import com.app.ui.height8
import com.app.ui.smallBodySmall
import com.app.ui.toCurrencyString

@Composable
fun BalanceHeaderComposable(
    balance: Double,
    totalIncome: Double,
    totalExpenses: Double,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = Strings.CURRENT_BALANCE,
            style = MaterialTheme.typography.smallBodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height8())

        Text(
            text = balance.toCurrencyString(withSymbol = true),
            style = MaterialTheme.typography.boldDisplayLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height24())

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IncomeExpenseCardComposable(
                title = Strings.INCOME,
                amount = totalIncome,
                isIncome = true,
            )

            IncomeExpenseCardComposable(
                title = Strings.EXPENSE,
                amount = totalExpenses,
                isIncome = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceHeaderComposablePreview() {
    BalanceHeaderComposable(
        balance = 5580.5,
        totalIncome = 3950.25,
        totalExpenses = -970.75,
    )
}
