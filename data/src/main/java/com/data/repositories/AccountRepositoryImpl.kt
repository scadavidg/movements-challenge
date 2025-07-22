package com.data.repositories

import com.data.api.AccountApiService
import com.domain.models.AccountDetail
import com.domain.models.Result
import com.domain.repositories.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl
    @Inject
    constructor(
        private val apiService: AccountApiService,
    ) : AccountRepository {
        override suspend fun getAccountDetail(): Result<AccountDetail> =
            try {
                val response = apiService.getAccountDetail()
                val accountDetail = response.record.toDomain()
                Result.Success(accountDetail)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
    }
