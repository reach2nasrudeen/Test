package com.interview.test

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.Calendar

class ExpiryDateEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    init {
        filters = arrayOf(InputFilter.LengthFilter(5))
        addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val mmYYFormat = "MMYY"

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() == current) return

                val clean = s.toString().replace("[^\\d]".toRegex(), "")
                val cleanC = current.replace("[^\\d]".toRegex(), "")

                val cl = clean.length
                var sel = cl
                for (i in 2..cl) {
                    sel++
                }

                if (clean == cleanC) sel--

                if (clean.length < 4) {
                    current = when (clean.length) {
                        2 -> clean.substring(0, 2) + "/"
                        3 -> clean.substring(0, 2) + "/" + clean.substring(2)
                        else -> clean
                    }
                } else {
                    current = clean.substring(0, 2) + "/" + clean.substring(2, 4)
                }

                current = if (clean.length < 4) current else clean.substring(0, 2) + "/" + clean.substring(2, 4)

                removeTextChangedListener(this)
                setText(current)
                setSelection(if (sel < current.length) sel else current.length)
                addTextChangedListener(this)

                validateDate()
            }

            private fun validateDate() {
                val text = text.toString()
                if (text.length == 5) {
                    val month = text.substring(0, 2).toIntOrNull()
                    val year = "20" + text.substring(3, 5)

                    if (month == null || month < 1 || month > 12) {
                        error = "Invalid month"
                        return
                    }

                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

                    val enteredYear = year.toInt()
                    if (enteredYear < currentYear || (enteredYear == currentYear && month < currentMonth)) {
                        error = "Invalid expiry date"
                    } else {
                        error = null // Clear error if valid
                    }
                }
            }
        })
    }
}