package com.quickdev.projectdashboard.util.assertions

import android.util.Patterns
import com.afollestad.vvalidator.assertion.Assertion
import com.google.android.material.textfield.TextInputLayout
import com.quickdev.projectdashboard.util.text

class PhoneAssertion internal constructor() : Assertion<TextInputLayout, PhoneAssertion>() {
    private val regex = Patterns.PHONE

    override fun isValid(view: TextInputLayout) = regex.matcher(view.text()).matches()

    override fun defaultDescription() = "must be a valid phone number"
}
