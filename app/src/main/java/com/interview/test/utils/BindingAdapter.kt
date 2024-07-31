package com.interview.test.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.interview.test.R
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

@BindingAdapter("tint_luminance")
fun setTintBasedOnBackground(textView: AppCompatImageView, luminance: Double) {
// Set text color based on luminance
    if (luminance > 0.5) {
        // Light background - set text color to black
        textView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
    } else {
        // Dark background - set text color to white
        textView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
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


@BindingAdapter("transaction_date")
fun setTransactionDate(textView: AppCompatTextView, transactionDate: String?) {
    textView.text = transactionDate?.let { formatDateTime(it) } ?: Constants.NOT_AVAILABLE
}

@BindingAdapter("visible_unless")
fun setVisibilityUnlessError(view: View, error: Boolean) {
    view.isVisible = error
}


@BindingAdapter("transaction_type")
fun setTransactionType(view: AppCompatImageView, type: String?) {
    view.setImageResource(
        if (type.equals("debit", true)) {
            R.drawable.ic_vd_sent
        } else R.drawable.ic_vd_received
    )
}