package com.quickdev.projectdashboard.util

import com.google.android.material.textfield.TextInputLayout

internal fun TextInputLayout.text(): String {
    return editText?.text?.toString() ?: ""
}