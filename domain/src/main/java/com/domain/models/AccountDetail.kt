package com.domain.models

data class AccountDetail(
    val balance: Double,
    val transactions: List<Transaction>,
)
