package com.data.dto

import com.domain.models.AccountDetail
import com.domain.models.Transaction
import com.domain.models.TransactionType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionResponseDto(
    @Json(name = "record") val record: RecordDto,
)

@JsonClass(generateAdapter = true)
data class RecordDto(
    @Json(name = "balance") val balance: Double,
    @Json(name = "transactions") val transactions: List<TransactionDto>,
) {
    fun toDomain(): AccountDetail =
        AccountDetail(
            balance = balance,
            transactions = transactions.map { it.toDomain() },
        )
}

@JsonClass(generateAdapter = true)
data class TransactionDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "date") val date: String,
    @Json(name = "type") val type: String,
) {
    fun toDomain(): Transaction =
        Transaction(
            id = id,
            name = name,
            amount = amount,
            date = date,
            type = TransactionType.from(type),
        )
}
