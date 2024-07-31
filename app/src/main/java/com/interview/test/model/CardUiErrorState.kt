package com.interview.test.model

import androidx.annotation.StringRes
import com.interview.test.R

data class CardUiErrorState(
    var error: Boolean = false,
    @StringRes var message: Int = R.string.text_error_invalid,
)

