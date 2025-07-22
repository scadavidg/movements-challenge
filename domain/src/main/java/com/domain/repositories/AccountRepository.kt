package com.domain.repositories

import com.domain.models.AccountDetail
import com.domain.models.Result

interface AccountRepository {
    suspend fun getAccountDetail(): Result<AccountDetail>
}
