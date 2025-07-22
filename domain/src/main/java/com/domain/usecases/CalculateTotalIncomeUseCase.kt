package com.domain.usecases

import com.domain.models.Transaction

interface CalculateTotalIncomeUseCase {
    suspend operator fun invoke(transactions: List<Transaction>): Double
}
