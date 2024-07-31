package com.interview.test.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class ExpiryDateEditText(context: Context, attrs: AttributeSet?) :
    AppCompatEditText(context, attrs) {

    init {
        filters = arrayOf(InputFilter.LengthFilter(5))
        addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString() == current) return

                val clean = s.toString().replace("\\D".toRegex(), "")
                val cleanC = current.replace("\\D".toRegex(), "")

                val cl = clean.length
                var sel = cl
                for (i in 2..cl) {
                    sel++
                }

                if (clean == cleanC) sel--

                current = if (clean.length < 4) {
                    when (clean.length) {
                        2 -> clean.substring(0, 2) + "/"
                        3 -> clean.substring(0, 2) + "/" + clean.substring(2)
                        else -> clean
                    }
                } else {
                    clean.substring(0, 2) + "/" + clean.substring(2, 4)
                }

                current = if (clean.length < 4) {
                    current
                } else {
                    clean.substring(0, 2) + "/" + clean.substring(2, 4)
                }

                removeTextChangedListener(this)
                setText(current)
                setSelection(if (sel < current.length) sel else current.length)
                addTextChangedListener(this)
            }
        })
    }
}