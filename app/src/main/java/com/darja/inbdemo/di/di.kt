package com.darja.inbdemo.di

import com.darja.inbdemo.data.HardcodedClientRepository
import com.darja.inbdemo.data.HardcodedCreditRulesRepository
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import com.darja.inbdemo.domain.usecase.GetLoanDecisionUseCase
import com.darja.inbdemo.ui.decision.DecisionActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val data = module {
    single<CreditRulesRepository> { HardcodedCreditRulesRepository() }
    single<ClientRepository> { HardcodedClientRepository() }
}

val domain = module {
    factory { GetLoanDecisionUseCase(get(), get()) }
}

val mvvm = module {
    viewModel { DecisionActivityViewModel(get()) }
}