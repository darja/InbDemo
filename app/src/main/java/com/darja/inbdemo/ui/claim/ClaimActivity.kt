package com.darja.inbdemo.ui.claim

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.darja.inbdemo.R
import com.darja.inbdemo.ui.decision.DecisionActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * This activity reads and validates user input.
 * If valid, it would pass values to the [DecisionActivity]
 */
class ClaimActivity: AppCompatActivity(), ClaimActivityView.Owner {
    private val viewModel: ClaimActivityViewModel by viewModel()
    private lateinit var view: ClaimActivityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim)

        view = findViewById(R.id.claim_root)
        view.owner = this

        setupView()
        observeViewModel()
    }

    private fun setupView() {
        view.setAmountHelperText(viewModel.getAmountHelperText())
        view.setPeriodHelperText(viewModel.getPeriodHelperText())
    }

    private fun observeViewModel() {
        viewModel.personalCodeError.observe(this, Observer { view.setPersonalCodeError(it) })
        viewModel.amountError.observe(this, Observer { view.setAmountError(it) })
        viewModel.periodError.observe(this, Observer { view.setPeriodError(it) })
    }

    override fun onCheckLoanButtonClick() {
        val personalCode = viewModel.validatePersonalCode(view.getPersonalCode())
        val amount = viewModel.validateAmount(view.getAmount())
        val period = viewModel.validatePeriod(view.getPeriod())

        if (personalCode != null && amount != null && period != null) {
            Log.i("Claim", "Input is valid")
        } else {
            Log.w("Claim", "Input is invalid")
        }
    }}