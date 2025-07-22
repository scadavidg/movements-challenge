package com.data.di

import com.data.usecases.CalculateTotalExpensesUseCaseImpl
import com.data.usecases.CalculateTotalIncomeUseCaseImpl
import com.data.usecases.GetAccountSummaryUseCaseImpl
import com.domain.usecases.CalculateTotalExpensesUseCase
import com.domain.usecases.CalculateTotalIncomeUseCase
import com.domain.usecases.GetAccountSummaryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetAccountDetailUseCase(impl: GetAccountSummaryUseCaseImpl): GetAccountSummaryUseCase

    @Binds
    abstract fun bindCalculateTotalIncomeUseCase(impl: CalculateTotalIncomeUseCaseImpl): CalculateTotalIncomeUseCase

    @Binds
    abstract fun bindCalculateTotalExpensesUseCase(impl: CalculateTotalExpensesUseCaseImpl): CalculateTotalExpensesUseCase
}
