package com.domain.models

data class Transaction(
    val id: Int,
    val name: String,
    val amount: Double,
    val date: String,
    val type: TransactionType,
)
