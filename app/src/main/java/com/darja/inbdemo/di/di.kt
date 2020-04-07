package com.darja.inbdemo.di

import com.darja.inbdemo.data.HardcodedClientRepository
import com.darja.inbdemo.data.HardcodedCreditRulesRepository
import com.darja.inbdemo.domain.repo.ClientRepository
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import com.darja.inbdemo.domain.usecase.GetLoanDecisionUseCase
import com.darja.inbdemo.ui.decision.DecisionActivityViewModel
import com.darja.inbdemo.util.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ResourceProvider(androidContext().resources) }

}

val decisionActivityModule = module {
    single<CreditRulesRepository> { HardcodedCreditRulesRepository() }
    single<ClientRepository> { HardcodedClientRepository() }

    factory { GetLoanDecisionUseCase(get(), get()) }

    viewModel { DecisionActivityViewModel(get(), get(), get()) }
}