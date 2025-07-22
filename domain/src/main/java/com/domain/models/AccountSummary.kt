package com.domain.models

data class AccountSummary(
    val balance: Double,
    val totalIncome: Double,
    val totalExpenses: Double,
    val transactions: List<Transaction>,
)
