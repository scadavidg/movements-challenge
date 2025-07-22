package com.data.usecases

import com.domain.models.Transaction
import com.domain.models.TransactionType
import com.domain.usecases.CalculateTotalIncomeUseCase
import javax.inject.Inject

class CalculateTotalIncomeUseCaseImpl
    @Inject
    constructor() : CalculateTotalIncomeUseCase {
        override suspend fun invoke(transactions: List<Transaction>): Double =
            transactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
    }
