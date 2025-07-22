import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ui.components.IncomeExpenseCardComposable
import com.app.ui.Strings

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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$${"%,.2f".format(balance)}",
            style =
                MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(24.dp))

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

