package com.interview.test.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlin.math.pow
import kotlin.random.Random


fun Context.getDrawableRes(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}


fun getRandomColor() = Color.argb(
    255,
    Random.nextInt(256),
    Random.nextInt(256),
    Random.nextInt(256)
)


fun calculateLuminance(color: Int): Double {
    var r = Color.red(color) / 255.0
    var g = Color.green(color) / 255.0
    var b = Color.blue(color) / 255.0

    // Apply the gamma correction
    r = if ((r <= 0.03928)) r / 12.92 else ((r + 0.055) / 1.055).pow(2.4)
    g = if ((g <= 0.03928)) g / 12.92 else ((g + 0.055) / 1.055).pow(2.4)
    b = if ((b <= 0.03928)) b / 12.92 else ((b + 0.055) / 1.055).pow(2.4)

    // Calculate luminance
    return 0.2126 * r + 0.7152 * g + 0.0722 * b
}

