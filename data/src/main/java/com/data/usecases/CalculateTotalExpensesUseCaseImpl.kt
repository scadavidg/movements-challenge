package com.data.usecases

import com.domain.models.Transaction
import com.domain.models.TransactionType
import com.domain.usecases.CalculateTotalExpensesUseCase
import javax.inject.Inject

class CalculateTotalExpensesUseCaseImpl
    @Inject
    constructor() : CalculateTotalExpensesUseCase {
        override suspend fun invoke(transactions: List<Transaction>): Double =
            transactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
    }
