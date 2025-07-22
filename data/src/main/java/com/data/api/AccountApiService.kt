package com.data.api

import com.data.dto.TransactionResponseDto
import retrofit2.http.GET

interface AccountApiService {
    companion object {
        const val ACCOUNT_DETAIL_PATH = "/v3/b/687e9f36f7e7a370d1eb9ee0"
    }

    @GET(ACCOUNT_DETAIL_PATH)
    suspend fun getAccountDetail(): TransactionResponseDto
}
