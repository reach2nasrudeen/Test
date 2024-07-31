package com.interview.test.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.interview.test.R
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import kotlin.math.pow
import kotlin.random.Random


fun Context.getDrawableRes(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

fun Context.getColorRes(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
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


inline fun <reified T> Context.getObjectFromJson(jsonFileName: String): T {
    val myJson = inputStreamToString(this.assets.open(jsonFileName))
    return Gson().fromJson(myJson, T::class.java)
}

fun inputStreamToString(inputStream: InputStream): String {
    return try {
        val bytes = ByteArray(inputStream.available())
        inputStream.read(bytes, 0, bytes.size)
        String(bytes)
    } catch (e: IOException) {
        ""
    }
}

inline fun <reified T> T.toModelString(): String? {
    return try {
        Gson().toJson(this)
    } catch (exception: Exception) {
        Timber.e(exception)
        null
    }
}

fun maskCardNumber(cardNumber: String): String {
    // Ensure the input is a valid card number length
    if (cardNumber.length != 16) return cardNumber

    // Take the last 4 digits
    val last4Digits = cardNumber.takeLast(4)

    // Mask the rest of the digits
    val maskedNumber = "**** **** **** $last4Digits"

    return maskedNumber
}

fun Context.showToast(@StringRes resId: Int) {
    showToast(getString(resId))
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context?.dpToPx(dipValue: Float): Float {
    return this?.let {
        val metrics = resources.displayMetrics
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    } ?: 0f

}

fun String?.removeWhiteSpace(): String {
    if (this.isNullOrEmpty()) return ""
    return this.replace("\\s+".toRegex(), "")
}


fun String?.trimmedOrEmpty() = this?.trim()?.trimIndent().orEmpty()