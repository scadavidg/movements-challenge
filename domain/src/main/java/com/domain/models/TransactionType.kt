package com.domain.models

enum class TransactionType(
    val rawValue: String,
) {
    INCOME("ingreso"),
    EXPENSE("egreso"),
    UNKNOWN("desconocido"),
    ;

    companion object {
        fun from(type: String): TransactionType =
            when (type.lowercase()) {
                INCOME.rawValue -> INCOME
                EXPENSE.rawValue -> EXPENSE
                else -> UNKNOWN
            }
    }
}
