package com.innoveloper.finpulse.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun dateToSting(dateInMillis: Long): String {
    val date = Date(dateInMillis)
    val today = Date()
    val yesterday = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
    val dateString = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(dateInMillis))
    return when (dateString) {
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(today) -> "Today"
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(yesterday) -> "Yesterday"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
    }
}

fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}