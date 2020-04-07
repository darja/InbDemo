package com.darja.inbdemo.ui.claim

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.darja.inbdemo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ClaimActivityView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var personalNumberContainerView: TextInputLayout
    private lateinit var personalNumberView: TextInputEditText

    private lateinit var amountContainerView: TextInputLayout
    private lateinit var amountView: TextInputEditText

    private lateinit var periodContainerView: TextInputLayout
    private lateinit var periodView: TextInputEditText

    private lateinit var checkButton: Button

    var owner: Owner? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        personalNumberView = findViewById(R.id.claim_personal_number)
        personalNumberContainerView = findViewById(R.id.claim_personal_number_container)

        amountView = findViewById(R.id.claim_amount)
        amountContainerView = findViewById(R.id.claim_amount_container)

        periodView = findViewById(R.id.claim_period)
        periodContainerView = findViewById(R.id.claim_period_container)

        checkButton = findViewById(R.id.claim_check_button)

        checkButton.setOnClickListener { owner?.onCheckLoanButtonClick() }
    }

    // region Personal code
    internal fun getPersonalCode() = personalNumberView.text.toString()

    internal fun setPersonalCodeError(error: String?) {
        if (personalNumberContainerView.error != null || error != null) {
            personalNumberContainerView.error = error
        }
    }
    // endregion

    // region Amount

    internal fun setAmountHelperText(text: String) {
        amountContainerView.helperText = text
    }

    internal fun setAmountError(error: String?) {
        if (amountContainerView.error != null || error != null) {
            amountContainerView.error = error
        }
    }

    internal fun getAmount() = amountView.text.toString()

    // endregion

    // region Period

    internal fun setPeriodHelperText(text: String) {
        periodContainerView.helperText = text
    }

    internal fun setPeriodError(error: String?) {
        if (periodContainerView.error != null || error != null) {
            periodContainerView.error = error
        }
    }

    internal fun getPeriod() = periodView.text.toString()

    // endregion

    interface Owner {
        fun onCheckLoanButtonClick()
    }

}