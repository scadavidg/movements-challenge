package com.app.ui.state

import com.domain.models.AccountSummary

sealed class MovementsUiState {
    object Loading : MovementsUiState()

    data class Success(
        val data: AccountSummary,
    ) : MovementsUiState()

    data class Error(
        val message: String,
    ) : MovementsUiState()
}
