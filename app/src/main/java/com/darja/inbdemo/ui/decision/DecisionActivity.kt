package com.darja.inbdemo.ui.decision

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.darja.inbdemo.R
import com.darja.inbdemo.domain.model.LoanClaim
import org.koin.androidx.viewmodel.ext.android.viewModel

class DecisionActivity : AppCompatActivity() {
    private val viewModel: DecisionActivityViewModel by viewModel()
    private lateinit var view: DecisionActivityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decision)

        view = findViewById(R.id.decision_root)

        observeViewModel()

        val claim = intent.extras?.getSerializable(EXTRA_CLAIM) as LoanClaim?
        if (claim != null) {
            viewModel.makeLoanDecision(claim)
        }
    }

    private fun observeViewModel() {
        viewModel.title.observe(this, Observer { view.setTitle(it) })
        viewModel.description.observe(this, Observer { view.setDescription(it) })
    }

    companion object {
        const val EXTRA_CLAIM = "claim"
    }
}
