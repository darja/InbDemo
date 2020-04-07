package com.darja.inbdemo.ui.decision

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.darja.inbdemo.R

internal class DecisionActivityView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()

        titleView = findViewById(R.id.decision_title)
        descriptionView = findViewById(R.id.decision_description)
    }

    internal fun setTitle(title: String) {
        titleView.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    internal fun setDescription(description: String) {
        descriptionView.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}