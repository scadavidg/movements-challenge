package com.data.usecases

import com.domain.models.AccountSummary
import com.domain.models.Result
import com.domain.repositories.AccountRepository
import com.domain.usecases.CalculateTotalExpensesUseCase
import com.domain.usecases.CalculateTotalIncomeUseCase
import com.domain.usecases.GetAccountSummaryUseCase
import javax.inject.Inject

class GetAccountSummaryUseCaseImpl
    @Inject
    constructor(
        private val repository: AccountRepository,
        private val calculateTotalIncomeUseCase: CalculateTotalIncomeUseCase,
        private val calculateTotalExpensesUseCase: CalculateTotalExpensesUseCase,
    ) : GetAccountSummaryUseCase {
        override suspend fun invoke(): Result<AccountSummary> =
            when (val result = repository.getAccountDetail()) {
                is Result.Success -> {
                    val balance = result.data.balance
                    val transactions = result.data.transactions

                    try {
                        val income = calculateTotalIncomeUseCase(transactions)
                        val expenses = calculateTotalExpensesUseCase(transactions)

                        Result.Success(
                            AccountSummary(
                                balance = balance,
                                totalIncome = income,
                                totalExpenses = expenses,
                                transactions = transactions,
                            ),
                        )
                    } catch (e: Exception) {
                        Result.Error(e.message ?: "Unexpected error during calculation")
                    }
                }

                is Result.Error -> Result.Error(result.message)
                Result.Loading -> Result.Loading
            }
    }
