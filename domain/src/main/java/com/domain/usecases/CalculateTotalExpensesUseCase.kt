package com.domain.usecases

import com.domain.models.Transaction

interface CalculateTotalExpensesUseCase {
    suspend operator fun invoke(transactions: List<Transaction>): Double
}
