package com.innoveloper.finpulse.ui.screen.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseListRoute(
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {
    val viewModel: ExpenseListViewModel = koinViewModel()
    val expenses by viewModel.expenses.collectAsState()
    val currentFilter by viewModel.currentDateFilter.collectAsState()

    ExpenseListScreen(
        expenses = expenses,
        currentFilter = currentFilter,
        onFilterChange = viewModel::setDateFilter,
        goToReport = goToReport,
        goToEntry = goToEntry
    )

}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseWithCategory> = emptyList(),
    currentFilter: DateFilterType,
    onFilterChange: (DateFilterType) -> Unit,
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {

    var groupByCategory by remember { mutableStateOf(false) }
    var showDateRangePicker by remember { mutableStateOf(false) } // State for the date range picker dialog
    val scope = rememberCoroutineScope()

    val bottomSheetState =
        rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)

    // The DateRangePicker Dialog
    if (showDateRangePicker) {
        val dateRangePickerState = rememberDateRangePickerState()
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDateRangePicker = false
                        // When confirmed, call the ViewModel function with the selected range
                        val startDate = dateRangePickerState.selectedStartDateMillis
                        val endDate = dateRangePickerState.selectedEndDateMillis
                        if (startDate != null && endDate != null) {
                            onFilterChange(DateFilterType.CustomRange(startDate, endDate))
                            // Close the bottom sheet as well
                            scope.launch { bottomSheetState.hide() }
                        }
                    },
                    // Enable button only when a full range is selected
                    enabled = dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) { Text("Cancel") }
            }
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
    }

    if (bottomSheetState.isVisible) {
        ModalBottomSheet(
            {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Filter Expenses",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val predefinedFilters = listOf(
                    DateFilterType.Today,
                    DateFilterType.Week,
                    DateFilterType.All
                )
                predefinedFilters.forEachIndexed { index, filterType ->
                    ListItem(
                        headlineContent = { Text(filterType.title) },
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = if (index == 0) 8.dp else 2.dp,
                                    topEnd = if (index == 0) 8.dp else 2.dp,
                                    bottomStart = 2.dp,
                                    bottomEnd = 2.dp
                                )
                            )
                            .clickable {
                                onFilterChange(filterType)
                                scope.launch { bottomSheetState.hide() }
                            }
                    )
                }

                ListItem(
                    headlineContent = { Text("Select Date Range...") },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 2.dp,
                                topEnd = 2.dp,
                                bottomStart = 8.dp,
                                bottomEnd = 8.dp
                            )
                        )
                        .clickable {
                            // Open the date range picker dialog
                            showDateRangePicker = true
                        }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentFilter.title) },
                actions = {
                    IconButton(onClick = { scope.launch { bottomSheetState.expand() } }) {
                        Icon(Icons.Default.CalendarToday, "Change Date Filter")
                    }
                    IconButton(onClick = goToReport) { Icon(Icons.Filled.Assessment, "Reports") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = goToEntry) {
                Icon(
                    Icons.Filled.Add,
                    "Add Expense"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (expenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = expenses.size,
                    total = expenses.sumOf { it.expense.amount },
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            AnimatedContent(
                targetState = expenses.isEmpty(),
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (groupByCategory) {
                            expenses.groupBy { it.category }.forEach { (category, expenses) ->
                                item {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                items(expenses, key = { it.expense.id }) { expense ->
                                    ExpenseListItem(expense, modifier = Modifier.animateItem())
                                }
                            }
                        } else {
                            items(expenses, key = { it.expense.id }) { expense ->
                                ExpenseListItem(expense, modifier = Modifier.animateItem())
                            }
                        }
                    }
                }
            }
        }
    }
}