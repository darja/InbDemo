package com.darja.inbdemo.ui.decision

import androidx.lifecycle.ViewModel
import com.darja.inbdemo.domain.usecase.GetLoanDecisionUseCase


class DecisionActivityViewModel(
    private val getLoanDecisionUseCase: GetLoanDecisionUseCase
) : ViewModel() {
}