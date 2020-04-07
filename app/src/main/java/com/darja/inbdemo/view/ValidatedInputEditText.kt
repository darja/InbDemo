package com.darja.inbdemo.view

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import com.darja.inbdemo.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class ValidatedTextInput(context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {

    private enum class ValidationEvent {
        LOST_FOCUS,
        TEXT_CHANGED
    }

    private val valueView: TextInputEditText

    private var validationEvent: ValidationEvent? = null

    /**
     * Validator should return a string error if input is invalid or null if input is valid
     */
    var validator: ((ValidatedTextInput) -> String?)? = null

    init {
        View.inflate(context, R.layout.view_validated_text_input, this)

        valueView = findViewById(R.id.validated_text_input_value)

        applyStyleableAttributes(context, attrs)

        setupEvents()
    }

    private fun setupEvents() {
        when (validationEvent) {
            ValidationEvent.LOST_FOCUS ->
                onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        validateValue()
                    }
                }

            ValidationEvent.TEXT_CHANGED ->
                valueView.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        text: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        validateValue()
                    }
                })
        }
    }

    private fun applyStyleableAttributes(context: Context, attrs: AttributeSet) {
        val attrValues = context.obtainStyledAttributes(attrs, R.styleable.ValidatedTextInput, 0, 0)

        // Hint
        val hint = attrValues.getString(R.styleable.ValidatedTextInput_android_hint)
        setHint(hint)

        // Max length
        val maxLength = attrValues.getInt(R.styleable.ValidatedTextInput_android_maxLength, 4)
        valueView.filters = arrayOf<InputFilter>(LengthFilter(maxLength))

        // Input format
        val inputType = attrValues.getInt(R.styleable.ValidatedTextInput_android_inputType, 4)
        valueView.inputType = inputType

        // Validation event
        if (attrValues.hasValue(R.styleable.ValidatedTextInput_validation_event)) {
            val validationEventCode =
                attrValues.getInt(R.styleable.ValidatedTextInput_validation_event, 0)
            validationEvent = ValidationEvent.values()[validationEventCode]
        } else {
            validationEvent = null
        }


        attrValues.recycle()
    }

    private fun validateValue() {
        error = validator?.invoke(this)


        refreshDrawableState()
    }

    fun getIntValue(): Int? {
        val inputValue = valueView.text?.trim()
        return if (inputValue.isNullOrEmpty()) {
            null
        } else {
            inputValue.toString().toIntOrNull()
        }
    }
}