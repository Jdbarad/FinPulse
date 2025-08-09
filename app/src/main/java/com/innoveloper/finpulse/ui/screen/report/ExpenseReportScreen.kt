package com.innoveloper.finpulse.ui.screen.report

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.ui.dateToSting
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.IOException
import java.util.Calendar

@Composable
fun ExpenseReportRoute(
    onBack: () -> Unit = {}
) {
    val viewModel: ExpenseReportViewModel = koinViewModel()
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(emptyList())
    ExpenseReportScreen(
        last7DaysExpenses = last7DaysExpenses,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    last7DaysExpenses: List<ExpenseWithCategory> = listOf(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // --- Data processing remains the same ---
    val dailyTotals = last7DaysExpenses
        .groupBy {
            val cal = Calendar.getInstance().apply { timeInMillis = it.expense.date }
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(
            Calendar.SECOND,
            0
        ); cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } }
        .toSortedMap(compareByDescending { it }) // Sort by most recent day

    val categoryTotals = last7DaysExpenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report (Last 7 Days)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReportSection("Daily Totals") {
                    if (dailyTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    dailyTotals.forEach { (dateMillis, total) ->
                        ReportRow(dateToSting(dateMillis), "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                ReportSection("Category Totals") {
                    if (categoryTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    categoryTotals.forEach { (category, total) ->
                        ReportRow(category.name, "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                // The MockBarChart still uses the calculated dailyTotals
                MockBarChart(dailyTotals)
            }

            // --- REPLACEMENT FOR EXPORT BUTTONS ---
            item {
                ExportButtons(
                    onPdfClick = {
                        coroutineScope.launch {
                            try {
                                val file = context.generatePdfReport(dailyTotals, categoryTotals)
                                context.shareFile(file, "application/pdf")
                            } catch (e: IOException) {
                                e.printStackTrace()
                                Toast.makeText(
                                    context,
                                    "Error generating PDF: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    onCsvClick = {
                        coroutineScope.launch {
                            try {
                                val file = context.generateCsvFile(last7DaysExpenses)
                                context.shareFile(file, "text/csv")
                            } catch (e: IOException) {
                                e.printStackTrace()
                                Toast.makeText(
                                    context,
                                    "Error generating CSV: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ExportButtons(
    onPdfClick: () -> Unit,
    onCsvClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onPdfClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("SHARE SUMMARY")
        }
        Button(
            onClick = onCsvClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("SHARE AS CSV")
        }
    }
}