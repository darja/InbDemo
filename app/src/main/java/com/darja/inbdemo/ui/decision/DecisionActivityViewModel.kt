package com.darja.inbdemo.ui.decision

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darja.inbdemo.R
import com.darja.inbdemo.domain.usecase.GetLoanDecisionUseCase

class DecisionActivityViewModel(
    private val getLoanDecisionUseCase: GetLoanDecisionUseCase
): ViewModel() {

    private val personalCodeErrorMutable = MutableLiveData<Int>()
    internal val personalCodeError = personalCodeErrorMutable

    fun validatePersonalCode(personalCode: String): String? {
        if (personalCode.length != 11) {
            personalCodeErrorMutable.postValue(R.string.error_personal_code_length)
            return null
        } else {
            return personalCode
        }
    }
}