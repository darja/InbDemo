package com.darja.inbdemo.ui.claim

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darja.inbdemo.R
import com.darja.inbdemo.domain.repo.CreditRulesRepository
import com.darja.inbdemo.util.ResourceProvider
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

class ClaimActivityViewModel(
    private val resourceProvider: ResourceProvider,
    private val creditRules: CreditRulesRepository
) : ViewModel() {

    private val personalCodeErrorMutable = MutableLiveData<String>()
    internal val personalCodeError = personalCodeErrorMutable

    private val amountErrorMutable = MutableLiveData<String>()
    internal val amountError = amountErrorMutable

    private val periodErrorMutable = MutableLiveData<String>()
    internal val periodError = periodErrorMutable

    /**
     * Reads and validates personal code from the user input. If invalid, posts error to [personalCodeError]
     *
     * @return String value if valid or null if input is incorrect
     */
    internal fun validatePersonalCode(personalCode: String): String? {
        if (personalCode.length != 11) {
            personalCodeErrorMutable.postValue(resourceProvider.getString(R.string.error_personal_code_length))
            return null
        } else {
            personalCodeErrorMutable.postValue(null)
            return personalCode
        }
    }

    /**
     * Reads and validates amount from the user input. If invalid, posts error to [amountError]
     *
     * @return Int value if valid or null if input is incorrect
     */
    internal fun validateAmount(amountString: String): Int? {
        val amount = amountString.toIntOrNull()
        if (amount == null ||
            amount < creditRules.getMinLoan() ||
            amount > creditRules.getMaxLoan()
        ) {
            amountErrorMutable.postValue(getAmountHelperText())
            return null
        } else {
            amountErrorMutable.postValue(null)
            return amount
        }
    }

    /**
     * Reads and validates loan period from the user input. If invalid, posts error to [periodError]
     *
     * @return Int value if valid or null if input is incorrect
     */
    internal fun validatePeriod(amountString: String): Int? {
        val period = amountString.toIntOrNull()
        if (period == null ||
            period < creditRules.getMinPeriod() ||
            period > creditRules.getMaxPeriod()
        ) {
            periodErrorMutable.postValue(getPeriodHelperText())
            return null
        } else {
            periodErrorMutable.postValue(null)
            return period
        }
    }

    fun getAmountHelperText(): String {
        val amountFormatter: DecimalFormat =
            NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
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