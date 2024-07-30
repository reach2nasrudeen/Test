package com.interview.test.utils

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


const val TF_DEFAULT = "yyyy-MM-dd'T'HH:mm:ssX"

const val TZ_UTC = "UTC"

fun String?.toDate(
    inputTimeFormat: String = TF_DEFAULT,
    inputTimeZone: String = getDefaultTimeZone(),
): Date {
    if (this.isNullOrEmpty()) return Date()
    val inputFormat = SimpleDateFormat(inputTimeFormat, Locale.getDefault()).apply {
        timeZone = inputTimeZone.getTimeZone()
    }
    try {
        return inputFormat.parse(this) ?: Date()
    } catch (e: ParseException) {
        Timber.e(e)
    }
    return Date()
}

fun String.getTimeZone(): TimeZone = TimeZone.getTimeZone(this)

private fun getDefaultTimeZone(): String {
    return Calendar.getInstance().timeZone.id ?: TZ_UTC
}

fun formatDateTime(dateString: String): String {
    // Define the input format
    val inputFormatter = DateTimeFormatter.ofPattern(TF_DEFAULT)
    // Parse the date string
    val dateTime = OffsetDateTime.parse(dateString, inputFormatter)

    // Get the device's default time zone ID
    val deviceTimeZone = ZoneId.systemDefault()


    // Define the output format for date and time
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MMM")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Format the date and time
    val formattedDate = dateTime.format(dateFormatter)
//    val formattedTime = dateTime.withOffsetSameInstant(ZoneOffset.UTC).format(timeFormatter).lowercase()

    val formattedTime = dateTime.atZoneSameInstant(deviceTimeZone).format(timeFormatter).lowercase()


    // Combine formatted date and time
    return "$formattedDate  |  $formattedTime"
}