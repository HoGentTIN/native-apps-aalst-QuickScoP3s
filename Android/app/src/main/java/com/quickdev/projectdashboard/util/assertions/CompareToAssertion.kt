package com.quickdev.projectdashboard.util.assertions

import com.afollestad.vvalidator.assertion.Assertion
import com.google.android.material.textfield.TextInputLayout
import com.quickdev.projectdashboard.util.text

class CompareToAssertion internal constructor(private val otherView: TextInputLayout) : Assertion<TextInputLayout, CompareToAssertion>() {
    private var ignoreCase: Boolean = false

    /** Case is ignored when checking if the input contains the string. */
    fun ignoreCase(): CompareToAssertion {
        ignoreCase = true
        return this
    }

    override fun isValid(view: TextInputLayout) = view.text().equals(otherView.text(), ignoreCase)

    override fun defaultDescription() = "must be equal to \"${otherView.text()}\""
}
