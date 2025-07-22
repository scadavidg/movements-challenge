package com.app.ui.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ui.state.MovementsUiState
import com.domain.models.Result
import com.domain.usecases.GetAccountSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.app.ui.Strings

@HiltViewModel
class MovementsViewModel
    @Inject
    constructor(
        private val getAccountDetailUseCase: GetAccountSummaryUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow<MovementsUiState>(MovementsUiState.Loading)
        val state: StateFlow<MovementsUiState> = _state

        init {
            loadData()
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun loadData() {
            viewModelScope.launch {
                _state.value = MovementsUiState.Loading
                try {
                    when (val result = getAccountDetailUseCase()) {
                        is Result.Success -> _state.value = MovementsUiState.Success(result.data)
                        is Result.Error -> _state.value = MovementsUiState.Error(result.message)
                        is Result.Loading -> _state.value = MovementsUiState.Loading
                    }
                } catch (e: Exception) {
                    _state.value = MovementsUiState.Error(e.message ?: Strings.UNKNOWN_ERROR)
                }
            }
        }
    }
