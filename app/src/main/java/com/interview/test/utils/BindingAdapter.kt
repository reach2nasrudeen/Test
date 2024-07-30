package com.interview.test.utils

import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.interview.test.base.Constants
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue


@BindingAdapter("bg_luminance")
fun setTextColorBasedOnBackground(textView: AppCompatTextView, luminance: Double) {
// Set text color based on luminance
    if (luminance > 0.5) {
        // Light background - set text color to black
        textView.setTextColor(Color.BLACK)
    } else {
        // Dark background - set text color to white
        textView.setTextColor(Color.WHITE)
    }
}

@BindingAdapter("amount")
fun setAmountText(textView: AppCompatTextView, amount: Double?) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val formattedAmount = (amount ?: 0.0).let {
        if (it < 0) {
            "-${numberFormat.format(it.absoluteValue)}"
        } else {
            "+${numberFormat.format(it)}"
        }
    }
    textView.text = formattedAmount
}

@BindingAdapter("balance")
fun setBalanceText(textView: AppCompatTextView, amount: Double?) {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val formattedAmount = (amount ?: 0.0).let {
        if (it < 0) {
            "-${numberFormat.format(it.absoluteValue)}"
        } else {
            numberFormat.format(it)
        }
    }
    textView.text = formattedAmount
}

@BindingAdapter("card_number")
fun setMaskedCardNumber(textView: AppCompatTextView, cardNumber: String?) {
    textView.text = maskCardNumber(cardNumber ?: Constants.NOT_AVAILABLE)
}

