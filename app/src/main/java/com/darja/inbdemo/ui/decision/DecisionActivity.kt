package com.darja.inbdemo.ui.decision

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DecisionActivity : AppCompatActivity() {
    private val viewModel: DecisionActivityViewModel by viewModel()
    private lateinit var view: DecisionActivityView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_claim)

//        view = findViewById(R.id.decision_root)
//        view.owner = this

        setupView()
    }

    private fun setupView() {
    }
}
