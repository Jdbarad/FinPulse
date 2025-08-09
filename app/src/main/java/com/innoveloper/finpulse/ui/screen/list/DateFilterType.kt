package com.innoveloper.finpulse.ui.screen.list

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class DateFilterType(open val title: String) {
    object Today : DateFilterType("Today's Expenses")
    object Week : DateFilterType("Last 7 Days")
    object All : DateFilterType("All Expenses")
    data class CustomRange(val startDate: Long, val endDate: Long) : DateFilterType("Custom Range") {
        override val title: String
            get() {
                val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                return "${sdf.format(Date(startDate))} - ${sdf.format(Date(endDate))}"
            }
    }
}