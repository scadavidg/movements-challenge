package com.domain.usecases

import com.domain.models.AccountSummary
import com.domain.models.Result

interface GetAccountSummaryUseCase {
    suspend operator fun invoke(): Result<AccountSummary>
}
