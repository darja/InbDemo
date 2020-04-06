package com.darja.inbdemo.ui.decision

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.darja.inbdemo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DecisionActivityView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var personalNumberView: TextInputEditText
    private lateinit var personalNumberContainerView: TextInputLayout
    private lateinit var amountView: TextInputEditText
    private lateinit var periodView: TextInputEditText
    private lateinit var checkButton: Button

    var owner: Owner? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        personalNumberView = findViewById(R.id.decision_personal_number)
        personalNumberContainerView = findViewById(R.id.decision_personal_number_container)
        amountView = findViewById(R.id.decision_amount)
        periodView = findViewById(R.id.decision_period)
        checkButton = findViewById(R.id.decision_check_button)

        checkButton.setOnClickListener { owner?.onCheckLoanButtonClick() }
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