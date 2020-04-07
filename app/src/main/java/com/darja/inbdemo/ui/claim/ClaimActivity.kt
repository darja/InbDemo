package com.darja.inbdemo.ui.claim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.darja.inbdemo.R
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        viewModel.personalCodeError.observe(this, Observer { view.setPersonalCodeError(getString(it)) })
    }

    override fun onCheckLoanButtonClick() {
        viewModel.validatePersonalCode(view.getPersonalCode())
    }}