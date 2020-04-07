package com.darja.inbdemo.ui.decision

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darja.inbdemo.R
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import com.darja.inbdemo.domain.usecase.GetLoanDecisionUseCase
import com.darja.inbdemo.util.ResourceProvider
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


class DecisionActivityViewModel(
    private val resourceProvider: ResourceProvider,
    private val creditRules: CreditRulesRepository,
    private val getLoanDecisionUseCase: GetLoanDecisionUseCase
) : ViewModel() {

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

    fun getAmountHelperText(): String {
        val amountFormatter: DecimalFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
        val symbols: DecimalFormatSymbols = amountFormatter.decimalFormatSymbols

        symbols.groupingSeparator = ' '
        amountFormatter.decimalFormatSymbols = symbols

        return resourceProvider.getString(
            R.string.helper_amount_format,
            amountFormatter.format(creditRules.getMinLoan()),
            amountFormatter.format(creditRules.getMaxLoan())
        )
    }

    fun getPeriodHelperText(): String {
        return resourceProvider.getString(
            R.string.helper_period_format,
            creditRules.getMinPeriod(),
            creditRules.getMaxPeriod()
        )
    }
}