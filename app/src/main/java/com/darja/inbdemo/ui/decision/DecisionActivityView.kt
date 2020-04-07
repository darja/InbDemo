package com.darja.inbdemo.ui.decision

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.darja.inbdemo.R
import com.darja.inbdemo.view.ValidatedTextInput
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DecisionActivityView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var personalNumberView: TextInputEditText
    private lateinit var personalNumberContainerView: TextInputLayout
    private lateinit var amountView: ValidatedTextInput
    private lateinit var periodView: TextInputEditText
    private lateinit var checkButton: Button

    var owner: Owner? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        personalNumberView = findViewById(R.id.decision_personal_number)
        personalNumberContainerView = findViewById(R.id.decision_personal_number_container)
        amountView = findViewById(R.id.decision_amount_container)
        periodView = findViewById(R.id.decision_period)
        checkButton = findViewById(R.id.decision_check_button)

        checkButton.setOnClickListener { owner?.onCheckLoanButtonClick() }

        amountView.validator = { view ->
            val amount = view.getIntValue()
            if (amount == null || amount < 2000 || amount > 10000) {
                "Amount should be between 2000 and 10000"
            } else {
                null
            }
        }
    }

    internal fun getPersonalCode(): String {
        return personalNumberView.text.toString()
    }

    internal fun setPersonalCodeError(error: String) {
        personalNumberContainerView.error = error
    }

    interface Owner {
        fun onCheckLoanButtonClick()
    }
}