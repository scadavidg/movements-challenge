package com.app.ui.screens

import BalanceHeaderComposable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.ui.components.SkeletonItemComposable
import com.app.ui.components.TransactionItemComposable
import com.app.ui.state.MovementsUiState
import com.app.ui.viewmodel.MovementsViewModel
import com.domain.models.AccountSummary
import com.domain.models.Transaction
import com.domain.models.TransactionType

@Composable
fun MovementsListComposable(modifier: Modifier) {
    val viewModel: MovementsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        when (val currentState = state) {
            is MovementsUiState.Loading -> {
                Column {
                    repeat(5) {
                        SkeletonItemComposable()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            is MovementsUiState.Success -> {
                with(currentState.data) {
                    BalanceHeaderComposable(
                        balance = balance,
                        totalIncome = totalIncome,
                        totalExpenses = totalExpenses,
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 1f),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(transactions) { transaction ->
                            TransactionItemComposable(transaction = transaction)
                        }
                    }
                }
            }

            is MovementsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = com.app.ui.Strings.ERROR_PREFIX + currentState.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovementsListComposablePreviewLoading() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            repeat(3) {
                SkeletonItemComposable()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovementsListComposablePreviewSuccess() {
    val sampleTransactions =
        listOf(
            Transaction(1, com.app.ui.Strings.SALARY, 3000.0, "2024-07-15", TransactionType.INCOME),
            Transaction(2, com.app.ui.Strings.MARKET, -150.0, "2024-07-16", TransactionType.EXPENSE),
            Transaction(3, com.app.ui.Strings.RENT, -950.0, "2024-07-17", TransactionType.EXPENSE),
        )

    val sampleData =
        AccountSummary(
            balance = 5580.5,
            totalIncome = 3450.0,
            totalExpenses = -1100.0,
            transactions = sampleTransactions,
        )

    MaterialTheme {
        Column {
            BalanceHeaderComposable(
                balance = sampleData.balance,
                totalIncome = sampleData.totalIncome,
                totalExpenses = sampleData.totalExpenses,
            )
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(sampleData.transactions) {
                    TransactionItemComposable(transaction = it)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovementsListComposablePreviewError() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = com.app.ui.Strings.ERROR_LOAD,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

