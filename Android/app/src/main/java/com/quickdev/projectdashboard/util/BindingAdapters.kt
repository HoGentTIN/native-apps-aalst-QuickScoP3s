package com.quickdev.projectdashboard.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.quickdev.projectdashboard.data.UserPictureHelper
import com.quickdev.projectdashboard.util.converters.DateFormatter
import java.time.LocalDate
import java.util.*

@BindingAdapter(value = ["srcStr", "srcDefault"], requireAll = false)
fun setImageSource(view: ImageView, source: String?, sourceDefault: Drawable?) {
    when {
        (source ?: "").isNotEmpty() -> {
            val img = UserPictureHelper.decodeImage(source)
            view.setImageBitmap(img)
        }
        sourceDefault != null -> view.setImageDrawable(sourceDefault)
        else -> view.setImageDrawable(null)
    }
}

/****** CALENDARVIEW BINDING ADAPTERS ******/

@BindingAdapter("android:localdate")
fun setDate(view: CalendarView, date: LocalDate?) {
    if (date != null) {
        val longDate = DateFormatter.toLong(date)
        if (longDate != view.date)
            view.date = longDate
    }
}

@BindingAdapter(value = ["android:onSelectedDayChange", "android:dateAttrChanged"], requireAll = false)
fun setListeners(calendarView: CalendarView, onDayChange: OnDateChangeListener?, attrChange: InverseBindingListener?) {
    if (attrChange == null)
        calendarView.setOnDateChangeListener(onDayChange)
    else {
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            onDayChange?.onSelectedDayChange(view, year, month, dayOfMonth)
            val instance = Calendar.getInstance()
            instance[year, month] = dayOfMonth
            view.date = instance.timeInMillis
            attrChange.onChange()
        }
    }
}

@InverseBindingAdapter(attribute = "android:localdate", event = "android:dateAttrChanged")
fun getDate(view: CalendarView): LocalDate {
    return DateFormatter.fromLong(view.date)
}

@BindingAdapter(value = ["selectedValue", "selectedValueAttrChanged"], requireAll = false)
fun bindSpinnerData(spinner: Spinner, newValue: Int?, newTextAttrChanged: InverseBindingListener) {

    spinner.onItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
            newTextAttrChanged.onChange()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    if (newValue != null)
        spinner.setSelection(newValue)
}

@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
fun captureSelectedValue(spinner: Spinner): Int {
    return spinner.selectedItemPosition
}