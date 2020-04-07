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

val app = module {
    single { ResourceProvider(androidContext().resources) }

}

// todo modules for features, not for layers
val data = module {
    single<CreditRulesRepository> { HardcodedCreditRulesRepository() }
    single<ClientRepository> { HardcodedClientRepository() }
}

val domain = module {
    factory { GetLoanDecisionUseCase(get(), get()) }
}

val mvvm = module {
    viewModel { DecisionActivityViewModel(get(), get(), get()) }
}